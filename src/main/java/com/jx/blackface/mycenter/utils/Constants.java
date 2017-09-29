package com.jx.blackface.mycenter.utils;

public final class Constants {
	public static enum ServiceStatus{
	/**
	 * 服务状态 ： 2，暂停服务 9，服务进行中 ； 10，服务完成 -2服务商取消 -3 客户取消  -4客服取消
	 */
		SUSPEND("暂停服务",2),PROCEED("服务进行中",9),FINISHED("服务完成",10),
		SERVER_CANCEL("服务商取消",-2),CUSTOMER_CANCEL("客户取消",-3),CUSTOMER_SERVICE_CANCEL("客服取消",-4);
		
		private String statusName;
		private int index;
		
		private ServiceStatus(String name,int index){
			this.statusName = name;
			this.index = index;
		}
		
		private int getIndex(){
			return index;
		}
		
		private String getStatusName(){
			return statusName;
		}
		
		public static String getServiceName(int index){
			for(ServiceStatus tmp : ServiceStatus.values()){
				if(tmp.getIndex() == index){
					return tmp.getStatusName();
				}
			}
			return "";
		}
		
	}
}
