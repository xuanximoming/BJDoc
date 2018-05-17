package com.android.hospital.temperature;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.android.hospital.constant.AppConstant;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartDemo extends Activity {

	private static final long HOUR = 3600 * 1000;

	private static final long DAY = HOUR * 24;

	private static final int NUM = 6;//代表有6条数据

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		String[] titles = new String[] {  "体温"  };


		List<Date[]> x = getTempTimeArr();
		List<double[]> y = getTempArr();



		XYMultipleSeriesDataset dataset = buildDateDataset(titles, x, y);

		int[] colors = new int[] { Color.BLUE };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

		setChartSettings(renderer, "体温图", "日期", "体温",
				x.get(0)[0].getTime(),
				//x.get(0)[NUM-1].getTime(),
				//x.get(0)[x.size()-1].getTime(),
				x.get(0)[x.get(0).length-1].getTime(),
				35, 42,
				Color.WHITE, Color.WHITE);

		View chart = ChartFactory.getTimeChartView(this, dataset, renderer, "yy-MM-dd hh:mm");
		setContentView(chart);
	}

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
												   List<double[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		int length = titles.length;// 有几条线
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i]);  // 根据每条线的名称创建
			double[] xV = (double[]) xValues.get(i); // 获取第i条线的数据
			double[] yV = (double[]) yValues.get(i);
			int seriesLength = xV.length; // 有几个点

			for (int k = 0; k < seriesLength; k++) // 每条线里有几个点
			{
				series.add(xV[k], yV[k]);
			}

			dataset.addSeries(series);
		}

		return dataset;
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
													 PointStyle[] styles, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			r.setFillPoints(fill);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
									String title, String xTitle, String yTitle, double xMin,
									double xMax, double yMin, double yMax, int axesColor,
									int labelsColor) {

		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);

		renderer.setGridColor(Color.GREEN);
		renderer.setShowGridX(true);
		renderer.setShowGridY(true);
		renderer.setXLabels(10);
		renderer.setYLabels(40);

	}

	protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
													   List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	/**
	 *
	 * @Title: getTempTimeArr
	 * @Description: TODO(得到温度对应的时间)
	 * @param @return    设定文件
	 * @return List<Date[]>    返回类型
	 * @throws
	 */
	public List<Date[]> getTempTimeArr(){
		String[] tempTimeArr=AppConstant.temperatureTimeArr;//温度时间
		List<Date[]> x = new ArrayList<Date[]>();
		Date[] dates = new Date[tempTimeArr.length];
		try {
			for (int i = 0; i < tempTimeArr.length; i++) {
				dates[i]=Util.converToDate(tempTimeArr[i]);
				DebugUtil.debug("转换后的时间为--->"+dates[i]);
			}
			x.add(dates);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return x;
	}
	/**
	 *
	 * @Title: getTempArr
	 * @Description: TODO(得到温度)
	 * @param @return    设定文件
	 * @return List<double[]>    返回类型
	 * @throws
	 */
	public List<double[]> getTempArr(){
		String[] tempArr=AppConstant.temperatureArr;//温度数组

		List<double[]> values = new ArrayList<double[]>();
		double[] doubles=new double[tempArr.length];
		for (int i = 0; i < tempArr.length; i++) {
			doubles[i]=Double.parseDouble(tempArr[i]);
			DebugUtil.debug("转换后的温度为--->"+doubles[i]);
		}
		values.add(doubles);
		return values;
	}

}
