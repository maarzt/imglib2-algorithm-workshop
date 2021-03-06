package net.imglib2.tutorials;

import java.io.IOException;

import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

import ij.IJ;
import ij.ImagePlus;

public class Sandbox
{
	public static void main( final String[] args ) throws IOException
	{
		String path = Sandbox.class.getResource( "/blobs.tif" ).getFile();
		final Img< UnsignedByteType > image = openImage( path );
		// show
		ImageJFunctions.show( image );
		// create and show an empty image
		final Img< IntType > img2 = ArrayImgs.ints( Intervals.dimensionsAsLongArray( image ) );
		ImageJFunctions.show( img2 );
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
