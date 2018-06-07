package net.imglib2.tutorials;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.roi.DefaultROITree;
import net.imagej.roi.ROITree;
import net.imglib2.Cursor;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.roi.MaskPredicate;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.ui.UIService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalMinima< T extends RealType<T> > implements Command
{

	@Parameter
	Img<T> input;

	@Parameter
	double sigma;

	@Parameter
	int radius;

	@Parameter
	UIService uiService;

	@Parameter
	ConvertService convertService;

	@Override
	public void run()
	{
		// blur
		final Img< DoubleType > blurred = ArrayImgs.doubles( Intervals.dimensionsAsLongArray(input) );
		Gauss3.gauss(sigma, Views.extendBorder(input), blurred);
		// create shape
		Shape shape = new HyperSphereShape( radius );
		// calculate minima
		List< Point > points = findMinima( blurred, shape );
		// show
		showPoints( input, points );
	}

	private List< Point > findMinima( Img< DoubleType > blurred, Shape shape )
	{
		List< Point > points = new ArrayList<>();
		// Beginning of the exercise:
		// 1. Get an infinite input image by using Views.extendBorder
		RandomAccessible< DoubleType > extended = null;
		// 2. On the infinite input image use shape.neighborhoods... to get an image with pixel type "Neighborhood".
		RandomAccessible<Neighborhood<DoubleType>> neighborhoods = null;
		// 3. For each pixel in the image:
		//    Check if the pixel value is smaller than all neighborhood pixel values.
		//    If this is the case, add the position to "points".

		// End fo the exercise
		return points;
	}

	private boolean isCenterSmallest( DoubleType center, Neighborhood< DoubleType > neighborhood )
	{
		boolean centerIsSmallest = true;
		for( DoubleType neighbor : neighborhood ) {
			if( neighbor.compareTo( center ) < 0 )
				centerIsSmallest = false;
		}
		return centerIsSmallest;
	}

	// Helper methods to show points on the image

	private <T extends NumericType<T>> void showPoints( Img<T> image, List< Point > points )
	{
		ImagePlus imagePlus = ImageJFunctions.wrap( image, "title" );
		imagePlus.setOverlay( pointsToOverlay( points ) );
		uiService.show( imagePlus );
	}

	private Overlay pointsToOverlay( List< Point > points )
	{
		List< MaskPredicate<?> > rois = points.stream().map( GeomMasks::pointMask ).collect( Collectors.toList() );
		ROITree tree = new DefaultROITree();
		tree.addROIs( rois );
		return convertService.convert( tree, Overlay.class );
	}

	// Main method to run the Command

	public static void main( final String[] args ) throws IOException
	{
		String path = LocalMinima.class.getResource( "/blobs.tif" ).getFile();
		ImageJ imageJ = new ImageJ();
		Dataset input = imageJ.scifio().datasetIO().open( path );
		imageJ.command().run( LocalMinima.class, true, "input", input, "sigma", 8.0, "radius", 8 );
	}

}

