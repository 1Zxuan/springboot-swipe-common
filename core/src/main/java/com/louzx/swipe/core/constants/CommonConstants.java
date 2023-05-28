package com.louzx.swipe.core.constants;

import com.louzx.swipe.core.entity.AbstractSwipeTask;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 */

public class CommonConstants {

	public static final String ZERO = "0";
	public static final String ONE = "1";
	public static final String TWO = "2";
	public static final String THREE = "3";
	public static final String FOUR = "4";
	public static final String FIVE = "5";
	public static final String SIX = "6";
	public static final String SEVEN = "7";
	public static final String EIGHT = "8";
	public static final String NINE = "9";
	public static final String TEN = "10";

	public static final String NOTIFY_SQL = "INSERT INTO push_info (content, summary, input_date, send_uid) VALUES (?, ?, ?, ?)  ";

	public static final Integer COUPON = 1;

	public static final String TRUE = "1";
	public static final String FALSE = "0";

	public static volatile boolean SYS_STOP = false;
	public static final String GOODS_ITEM = "商品：【%s】，数量：【%s】<br/>";

	public static final int DEF_PAGE_SIZE=10; //默认每页行数
	public static final int DEF_PAGE_INDEX=1; //默认当前页号
	
	/**
	 * bootgrid分页相关默认设置
	 */
	public static final Integer BOOTGRID_CURRENT_DEFAULT = 1;//当前页
	public static final Integer BOOTGRID_ROWCOUNT_DEFAULT = 10;//每页记录数
	
	/**
	 * 数据源标志类型
	 */
	public interface DATASOURCE_TYPE{
		/**
		 * 默认
		 */
		public static final String DEFAULT = "default";
	}

	/**
	 * 动态表明
	 * */
	public static final String DYNAMIC = "${dynamic}";

	@Deprecated
	private static final Set<AbstractSwipeTask> RUN_TASKS = new HashSet<>();

	@Deprecated
	public static synchronized void addRunTask (AbstractSwipeTask swipeTask) {
		for (AbstractSwipeTask runTask : RUN_TASKS) {
			if (StringUtils.equals(runTask.getId(), swipeTask.getId())) {
				return;
			}
		}
		RUN_TASKS.add(swipeTask);
	}

	@Deprecated
	public static synchronized void removeRunTask(AbstractSwipeTask swipeTask) {
		Iterator<AbstractSwipeTask> iterator = RUN_TASKS.iterator();
		while (iterator.hasNext()) {
			AbstractSwipeTask task = iterator.next();
			if (StringUtils.equalsIgnoreCase(swipeTask.getId(), task.getId())) {
				iterator.remove();
				break;
			}
		}
	}

	@Deprecated
	public static synchronized void stopTask(AbstractSwipeTask swipeTask) {
		Iterator<AbstractSwipeTask> iterator = RUN_TASKS.iterator();
		while (iterator.hasNext()) {
			AbstractSwipeTask task = iterator.next();
			if (StringUtils.equalsIgnoreCase(swipeTask.getId(), task.getId())) {
				if (!task.isStop()) {
					task.setStop(true);
					iterator.remove();
					break;
				}
			}
		}
	}

}
