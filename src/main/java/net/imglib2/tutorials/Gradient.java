package net.imglib2.tutorials;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class Gradient
{
	public static void main( final String[] args )
	{
		String path = Sandbox.class.getResource( "/blobs.tif" ).getFile();
		// setup images
		final Img< UnsignedByteType > image = openImage( path );
		final RandomAccessibleInterval< IntType > result = ArrayImgs.ints( Intervals.dimensionsAsLongArray( image ));
		final RandomAccessibleInterval< UnsignedByteType > front = Views.interval( Views.extendBorder( image ), Intervals.translate( result, 1, 0 ) );
		// Beginning of the exercise:
		// Use LoopBuilder to calculate result = front - image;



		// End of the exercise
		// show
		ImageJFunctions.show( result );
	}

	private static <T extends NumericType<T> & NativeType<T> > Img< T > openImage( String path )
	{
		final ImagePlus imp = IJ.openImage( path );
		final Img< T > img = ImageJFunctions.wrap( imp );
		final Object type = Util.getTypeFromInterval( img );
		System.out.println( "Pixel Type: " + type.getClass() );
		System.out.println( "Img Type: " + img.getClass() );
		return img;
	}
}
