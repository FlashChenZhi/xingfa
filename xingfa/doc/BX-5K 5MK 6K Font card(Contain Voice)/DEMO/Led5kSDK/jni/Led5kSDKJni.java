package com.jni;

public class Led5kSDKJni {

	static {
		System.loadLibrary("Led5kSDK");
	}
	//串口停止位
	public static byte COM_ONESTOPBIT = 0;
	public static byte COM_ONE5STOPBITS = 1;
	public static byte COM_TWOSTOPBITS = 2;
    //串口校验模式
	public static byte COM_NOPARITY = 0;
	public static byte COM_ODDPARITY = 1;
	public static byte COM_EVENPARITY = 2;
	public static byte COM_MARKPARITY = 3;
	public static byte COM_SPACEPARITY = 4;
    //串口数据位
	public static byte COM_4BITS = 4;
	public static byte COM_5BITS = 5;
	public static byte COM_6BITS = 6;
	public static byte COM_7BITS = 7;
	public static byte COM_8BITS = 8;
    //控制器类型
	public static byte BX_Any = (byte) 0xFE;
	public static byte BX_5K1 = (byte) 0x51;
	public static byte BX_5K2 = (byte) 0x58;
	public static byte BX_5MK2 = (byte) 0x53;
	public static byte BX_5MK1 = (byte) 0x54;
	//节目内区域定义
	public static class Led5kArea {
		public short AreaX;		
		public short AreaY;		
		public short AreaWidth;
		public short AreaHeight;
		public byte Lines_sizes;
		public byte RunMode;
		public short Timeout;
		public byte SingleLine;
		public byte NewLine;
		public byte DisplayMode;
		public byte ExitMode;
		public byte Speed;
		public byte StayTime;
		public String text;
	}
	//初始化动态库
	public native void InitSdk();
	//释放动态库
	public native void ReleaseSdk();
	//创建广播模式通讯
	public native int CreateBroadCast(String broad_ip, int broad_port,
			byte card_type);
	//创建固定IP模式通讯
	public native int CreateClient(String led_ip, int led_port, byte card_type);
	//创建TCP Modbus通讯
	public native int CreateTcpModbus(String led_ip, byte card_type);
	//创建串口通讯
	public native int CreateComClient(byte com, int baudrate, byte card_type,
			short ScreenID);
	//创建串口Modbus通讯
	public native int CreateComModbus(byte com, int baudrate, byte Parity,
			byte DataBits, byte StopBits, byte card_type, short ScreenID);
	//销毁通讯
	public native void Destroy(int dwHand);
	//设置通讯超时
	public native void SetTimeout(int dwHand, int nSec);
	//Ping控制器
	public native int CON_PING(int dwHand);
	//复位控制器
	public native int CON_Reset(int dwHand);
	//控制器校时
	public native int CON_SytemClockCorrect(int dwHand);
	//检查控制器固件版本
	public native int CON_CheckCurrentFirmware(int dwHand,
			String FirmwareName, String FirmwareVersion, String FirmwareDateTime);
	//激活固件
	public native int CON_FirmwareActivate(int dwHand, String FirmwareName);
	//设置屏号
	public native int CON_SetScreenID(int dwHand, short newScreenID);
	//读取屏号
	public native int CON_ReadScreenID(int dwHand, Integer pScreenID);
	//强制开关机 OnOffFlag 1 开机、OnOffFlag 2关机
	public native int SCREEN_ForceOnOff(int dwHand, byte OnOffFlag);
	//定时开关机
	public native int SCREEN_TimeTurnOnOff(int dwHand, byte[] pTimer,
			int nGroup);
	//设置亮度
	public native int SCREEN_SetBrightness(int dwHand, byte BrightnessType,
			byte CurrentBrightness, byte[] BrightnessValue);
	//设置等待时间
	public native int SCREEN_SetWaitTime(int dwHand, byte WaitTime);
	/*锁定节目 	
	 * 		LockFlag：锁定状态，0x00——解锁状态，  0x01——锁定状态 ；0x00——解锁状态；
			StoreMode 锁定状态保存方式：0x00——掉电不保存，0x01——掉电保存；
			ProgramFileName	节目文件名	  
	*/
	public native int SCREEN_LockProgram(int dwHand, byte LockFlag,
			byte StoreMode, String ProgramFileName);
	//删除动态区
	//DeleteAreaId 动态区编号
	public native int SCREEN_DelDynamicArea(int dwHand, byte DeleteAreaId);

	/*	送动态区域
	 *
		    参    数                                                                 数据长度                                                        默认值                                 描述 
		 AreaX                   2                                                                                                                 区域 X 坐标，以字节(8 个像素点)为单位
         AreaY                   2                                区域 Y 坐标，以像素点为单位
         AreaWidth               2                                区域宽度，以字节(8 个像素点)为单位
         AreaHeight              2                                区域高度，以像素点为单位
         DynamicAreaLoc          1                   0xFF         动态区域编号
                                                                                                                                                                                                                                        注意：该参数只对动态区有效，其他区域为默认
                                                                                                                                                                                                                                        值，动态区必须统一编号，编号从 0 开始递增。
         Lines_sizes             1                                行间距
         RunMode                 1                                动态区运行模式
                                                                   0— 动态区数据循环显示。
                                                                   1— 动态区数据显示完成后静止显示最后一页数据。
                                                                   2— 动态区数据循环显示，超过设定时间后数据仍未更新时删除动态区信息。
         Timeout                 2                                动态区数据超时时间，单位为秒
         Reserved                3                   0x00         保留字
         SingleLine              1                   0x02         是否单行显示
                                                                    0x01——单行显示
                                                                    0x02——多行显示
        NewLine                  1                                是否自动换行
                                                                    0x01——不自动换行，显示数据在换行时必须插入换行符
                                                                    0x02——自动换行，显示内容不需要换行符，但是只能使用统一的中文字体和英文字体
        DisplayMode              1                                显示方式，其定义如下：
                                                                    0x01——静止显示
                                                                    0x02——快速打出
                                                                    0x03——向左移动
                                                                    0x04——向右移动
                                                                    0x05——向上移动
                                                                    0x06——向下移动
      ExitMode                   1                  0x00          退出方式
      Speed                      1                                显示速度，定义如下：
                                                                    0x00——最快
                                                                    0x01——
                                                                      ……
                                                                    0x17——
                                                                    0x18 ——最慢
      StayTime                   1                                显示特技停留时间，单位为 0.5s
      TextLen                                                     显示区域长度
      String AreaText                                             显示区域内容
		 * 
		 */
	public native int SCREEN_SendDynamicArea(int dwHand, short AreaX,
			short AreaY, short AreaWidth, short AreaHeight,
			byte DynamicAreaLoc, byte Lines_sizes, byte RunMode, short Timeout,
			byte SingleLine, byte NewLine, byte DisplayMode, byte ExitMode,
			byte Speed, byte StayTime,String AreaText);
	
	public native int SCREEN_Test(int dwHand, byte TestTime);
	//取消定是开关机
	public native int SCREEN_CancelTimeOnOff(int dwHand);
	
	public native int BX5MK_SetSpecialAppDynamic(int dwHand, short AreaX,
			short AreaY, short AreaW, short AreaH, byte DataType,
			byte Pagetotal, byte RunState, short Timeout, byte SingleLine,
			byte Lines_sizes, byte NewLine, short StayTime);
	
	public native int BX5MK_SendPageData(int dwHand, byte PageNum,
			short PageDataLen, byte[] PageData);

	public native int BX5MK_SendLatticeMessage(int dwHand, byte BlockFlag,
			short BlockAddr, byte[] BlockData, short BlockDataLen);

	public native int BX5MK_DelSpecialAppDynamic(int dwHand);
	//设置IP地址
	public native int BX5MK_SetIPAddress(int dwHand, byte ConnnectMode,
			String ip, String SubnetMask, String Gateway, short port,
			byte ServerMode, String ServerIPAddress, short ServerPort,
			String ServerAccessPassword, short HeartBeatInterval, String NetID);
	//设置MAC地址
	public native int BX5MK_SetMACAddress(int dwHand, byte[] MAC);

	public native int BX5MK_WebSearch(int dwHand, byte[] Status, byte[] Error,
			String IP, String SubNetMask, String Gate, byte[] Port, String Mac,
			String NetID);

	public native int BX5MK_DelPageData(int dwHand, byte PageLog);
	//格式化文件系统
	public native int OFS_Formatting(int dwHand);
	//删除节目
	public native int OFS_DeleteFile(int dwHand, String pFileName);
	//开始发送多个节目
	public native int OFS_BeginSendMultiFiles(int dwHand);
	//发送节目文件
	public native int OFS_SendFile(int dwHand, byte overwrite, String pFilePath);
	//发送节目数据
	/*
	  		参数 				数据长度 			默认值 				描述
	  	 pFileName 			  4 							PXXX 文件名XXX 为文件编号， ASCII 码表示
													           	文件名为字符串，发送按顺序发送，如“ P123”则先发送‘ P’，
													           	最后发送‘ 3’。开机 LOGO 文件名为“ LOGO”。FileLen 4 文件长度
	  DisplayType 			  2 							节目播放方式
	  														0——顺序播放；其他——定长播放的时间，单位为秒
	  PlayTimes			  	  1 							节目重复播放次数
	  ProgramLife			  8 							节目生命周期，发送顺序为：StartYear,StartMonth,StartDay,EndYear,EndMonth,EndDay
																起始年(2)+起始月(1)+起始日(1)+ 结束年(2)+结束月(1)+结束日(1)
																注：
																1. 时间均采用 BCD 码的方式
																2. 年范围为 0x1900—0x2099， 0xffff 为永久有效，先发送 LSB，后发送 MSB
	 ProgramWeek 			  1 							节目的星期属性
	 															1. Bit0 为 1 表示一周中的每一天都播放。
	 															2. Bit0 为 0 时，需判断 bit1-bit7 的来决定每天播放， bit1-bit7 依次表示周一到周日。
	 															3. 比特为 0 表示禁止播放，为 1 表示播放。
	 IsPlayOnTime 			  1								 1定时节目位   0 非定时注：为 0 时则播放时段组数默认值为 0 
	 Periodlist                                              节目周期列表 
	 Arealist												   节目内区域定义
	 */
	
	public native  int OFS_SendFileData(int dwHand, byte overwrite, String pFileName, 
			short DisplayType, byte PlayTimes, boolean IsValidAlways,
			short StartYear,byte StartMonth,byte StartDay,short EndYear,byte EndMonth,byte EndDay,
			byte ProgramWeek, boolean IsPlayOnTime, byte[] Periodlist, Led5kArea[] Arealist);
	//结束发送多个节目
	public native int OFS_EndSendMultiFiles(int dwHand);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Led5kSDKJni led = new Led5kSDKJni();

		byte[] bt = new byte[16];
		bt[0] = (byte) 0xff;
		bt[1] = (byte) 0xff;
		bt[2] = (byte) 0xff;
		bt[3] = (byte) 0xff;

		String st = "192.168.5.235";
		led.InitSdk();
		System.out.println("init ok");
		int hand = led.CreateClient(st, 5005, BX_5MK1);
		System.out.println(hand);
		led.SetTimeout(hand, 5);
		int ret = led.CON_PING(hand);
		System.out.println(ret);
		led.SCREEN_ForceOnOff(hand, (byte) 1);
		Integer id=new Integer(4);
		led.CON_ReadScreenID(hand, id);
		System.out.println(id);
		/* 发送动态区域
		led.SCREEN_SendDynamicArea(hand,(short)0,(short)0,(short)128,(short)52,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG1");	
		
		led.SCREEN_SendDynamicArea(hand,(short)128,(short)0,(short)128,(short)52,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG2");
		
		led.SCREEN_SendDynamicArea(hand,(short)256,(short)0,(short)128,(short)52,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG3");
		
		led.SCREEN_SendDynamicArea(hand,(short)0,(short)52,(short)384,(short)12,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG4");
		
		led.SCREEN_SendDynamicArea(hand,(short)0,(short)0,(short)64,(short)32,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x03,(byte)0x00,(byte)0x02,(byte)0x00,"上海仰邦软件科技有限公司");
				*/
		led.SCREEN_DelDynamicArea(hand, (byte)1);
		led.OFS_BeginSendMultiFiles(hand);
		byte overwrite=1;
		byte []Periodlist=new byte[6];
		Periodlist[0]=13;
		Periodlist[1]=30;
		Periodlist[2]=0;
		Periodlist[3]=15;
		Periodlist[4]=30;
		Periodlist[5]=0;

		Led5kArea[] Arealist={new Led5kArea()};

		Arealist[0].AreaX=(short)0;				
		Arealist[0].AreaY=(short)0;				
		Arealist[0].AreaWidth=(short)64;		
		Arealist[0].AreaHeight=(short)32;		
		Arealist[0].Lines_sizes=(byte)0;		
		Arealist[0].RunMode=(byte)0;			
		Arealist[0].Timeout=(short)2;
		Arealist[0].SingleLine=(byte)0x02;
		Arealist[0].NewLine=(byte)0x02;
		Arealist[0].DisplayMode=(byte)0x03;
		Arealist[0].ExitMode=(byte)0x00;
		Arealist[0].Speed=(byte)0x05;
		Arealist[0].StayTime=(byte)0x01;
		Arealist[0].text="发送节目";
		//发送节目
		led.OFS_SendFileData(hand, overwrite, "P001", (short)0, (byte)2, true, (short)0, 
				(byte)0, (byte)0,(short)0, (byte)0, (byte)0, (byte)1, true, Periodlist, 
				Arealist);
		led.OFS_EndSendMultiFiles(hand);
		led.Destroy(hand);
		led.ReleaseSdk();
	}
}
