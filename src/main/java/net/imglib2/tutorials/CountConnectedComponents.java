package net.imglib2.tutorials;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.algorithm.labeling.ConnectedComponents;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

import java.io.IOException;

public class CountConnectedComponents
{
	public static void main( final String[] args ) throws IOException
	{
		String path = CountConnectedComponents.class.getResource( "/blobs.tif" ).getFile();
		final Img< UnsignedByteType > image = openImage( path );
		// show
		ImageJFunctions.show( image );
		// threshold
		image.forEach( pixel -> pixel.setInteger( pixel.getInteger() < 127 ? 255 : 0 ) );
		// connected components
		Img< IntType > output = ArrayImgs.ints( Intervals.dimensionsAsLongArray( image ) );
		ConnectedComponents.labelAllConnectedComponents( image, output, ConnectedComponents.StructuringElement.FOUR_CONNECTED );
		// count components = maximum
		int max = 0;
		for( IntType pixel : output )
			max = Math.max( max, pixel.getInteger() );
		// output maximum
		System.out.println(max);
	}

	private static <T extends NumericType<T> & NativeType<T>> Img< T > openImage( String path )
	{
		final ImagePlus imp = IJ.openImage( path );
		final Img< T > img = ImageJFunctions.wrap( imp );
		final Object type = Util.getTypeFromInterval( img );
		System.out.println( "Pixel Type: " + type.getClass() );
		System.out.println( "Img Type: " + img.getClass() );
		return img;
	}
}
