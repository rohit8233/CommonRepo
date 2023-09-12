package com.reports.Runner;

import java.awt.Color;
import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart {
   

@SuppressWarnings("removal")
public static void main( String[ ] args ) throws Exception {
      DefaultPieDataset dataset = new DefaultPieDataset( );
      dataset.setValue("IPhone 5s", new Double( 20 ) );
      dataset.setValue("Samsung", 10);
      dataset.setValue("MotoG", new Double( 40 ) );
      dataset.setValue("Nokia Lumia", new Double( 10 ) );

      JFreeChart chart = ChartFactory.createPieChart3D(
         "",   // chart title
         dataset,          // data
         true,             // include legend
         true,
         false);
      final PiePlot3D plot = ( PiePlot3D ) chart.getPlot( );             
      plot.setStartAngle( 270 );             
      plot.setForegroundAlpha( 0.60f );             
      plot.setInteriorGap( 0.02 );
      plot.setSectionPaint("IPhone 5s", Color.BLACK);
      plot.setSectionPaint("Samsung", Color.GREEN);
      plot.setSectionPaint("MotoG", Color.YELLOW);
      plot.setSectionPaint("Nokia Lumia", Color.PINK);
     
      chart.setBorderVisible(false);
      chart.getPlot().setOutlinePaint(Color.WHITE);
      chart.getPlot().setBackgroundPaint(Color.WHITE);
      int width = 500;   /* Width of the image */
      int height = 250;  /* Height of the image */ 
      File pieChart = new File( "PieChart23.jpeg" ); 
      ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
   }
}