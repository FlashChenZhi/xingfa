package com.jni;

public class Led5kSDKJni {

	static {
		System.loadLibrary("Led5kSDK");
	}
	//����ֹͣλ
	public static byte COM_ONESTOPBIT = 0;
	public static byte COM_ONE5STOPBITS = 1;
	public static byte COM_TWOSTOPBITS = 2;
    //����У��ģʽ
	public static byte COM_NOPARITY = 0;
	public static byte COM_ODDPARITY = 1;
	public static byte COM_EVENPARITY = 2;
	public static byte COM_MARKPARITY = 3;
	public static byte COM_SPACEPARITY = 4;
    //��������λ
	public static byte COM_4BITS = 4;
	public static byte COM_5BITS = 5;
	public static byte COM_6BITS = 6;
	public static byte COM_7BITS = 7;
	public static byte COM_8BITS = 8;
    //����������
	public static byte BX_Any = (byte) 0xFE;
	public static byte BX_5K1 = (byte) 0x51;
	public static byte BX_5K2 = (byte) 0x58;
	public static byte BX_5MK2 = (byte) 0x53;
	public static byte BX_5MK1 = (byte) 0x54;
	//��Ŀ��������
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
	//��ʼ����̬��
	public native void InitSdk();
	//�ͷŶ�̬��
	public native void ReleaseSdk();
	//�����㲥ģʽͨѶ
	public native int CreateBroadCast(String broad_ip, int broad_port,
			byte card_type);
	//�����̶�IPģʽͨѶ
	public native int CreateClient(String led_ip, int led_port, byte card_type);
	//����TCP ModbusͨѶ
	public native int CreateTcpModbus(String led_ip, byte card_type);
	//��������ͨѶ
	public native int CreateComClient(byte com, int baudrate, byte card_type,
			short ScreenID);
	//��������ModbusͨѶ
	public native int CreateComModbus(byte com, int baudrate, byte Parity,
			byte DataBits, byte StopBits, byte card_type, short ScreenID);
	//����ͨѶ
	public native void Destroy(int dwHand);
	//����ͨѶ��ʱ
	public native void SetTimeout(int dwHand, int nSec);
	//Ping������
	public native int CON_PING(int dwHand);
	//��λ������
	public native int CON_Reset(int dwHand);
	//������Уʱ
	public native int CON_SytemClockCorrect(int dwHand);
	//���������̼��汾
	public native int CON_CheckCurrentFirmware(int dwHand,
			String FirmwareName, String FirmwareVersion, String FirmwareDateTime);
	//����̼�
	public native int CON_FirmwareActivate(int dwHand, String FirmwareName);
	//��������
	public native int CON_SetScreenID(int dwHand, short newScreenID);
	//��ȡ����
	public native int CON_ReadScreenID(int dwHand, Integer pScreenID);
	//ǿ�ƿ��ػ� OnOffFlag 1 ������OnOffFlag 2�ػ�
	public native int SCREEN_ForceOnOff(int dwHand, byte OnOffFlag);
	//��ʱ���ػ�
	public native int SCREEN_TimeTurnOnOff(int dwHand, byte[] pTimer,
			int nGroup);
	//��������
	public native int SCREEN_SetBrightness(int dwHand, byte BrightnessType,
			byte CurrentBrightness, byte[] BrightnessValue);
	//���õȴ�ʱ��
	public native int SCREEN_SetWaitTime(int dwHand, byte WaitTime);
	/*������Ŀ 	
	 * 		LockFlag������״̬��0x00��������״̬��  0x01��������״̬ ��0x00��������״̬��
			StoreMode ����״̬���淽ʽ��0x00�������粻���棬0x01�������籣�棻
			ProgramFileName	��Ŀ�ļ���	  
	*/
	public native int SCREEN_LockProgram(int dwHand, byte LockFlag,
			byte StoreMode, String ProgramFileName);
	//ɾ����̬��
	//DeleteAreaId ��̬�����
	public native int SCREEN_DelDynamicArea(int dwHand, byte DeleteAreaId);

	/*	�Ͷ�̬����
	 *
		    ��    ��                                                                 ���ݳ���                                                        Ĭ��ֵ                                 ���� 
		 AreaX                   2                                                                                                                 ���� X ���꣬���ֽ�(8 �����ص�)Ϊ��λ
         AreaY                   2                                ���� Y ���꣬�����ص�Ϊ��λ
         AreaWidth               2                                �����ȣ����ֽ�(8 �����ص�)Ϊ��λ
         AreaHeight              2                                ����߶ȣ������ص�Ϊ��λ
         DynamicAreaLoc          1                   0xFF         ��̬������
                                                                                                                                                                                                                                        ע�⣺�ò���ֻ�Զ�̬����Ч����������ΪĬ��
                                                                                                                                                                                                                                        ֵ����̬������ͳһ��ţ���Ŵ� 0 ��ʼ������
         Lines_sizes             1                                �м��
         RunMode                 1                                ��̬������ģʽ
                                                                   0�� ��̬������ѭ����ʾ��
                                                                   1�� ��̬��������ʾ��ɺ�ֹ��ʾ���һҳ���ݡ�
                                                                   2�� ��̬������ѭ����ʾ�������趨ʱ���������δ����ʱɾ����̬����Ϣ��
         Timeout                 2                                ��̬�����ݳ�ʱʱ�䣬��λΪ��
         Reserved                3                   0x00         ������
         SingleLine              1                   0x02         �Ƿ�����ʾ
                                                                    0x01����������ʾ
                                                                    0x02����������ʾ
        NewLine                  1                                �Ƿ��Զ�����
                                                                    0x01�������Զ����У���ʾ�����ڻ���ʱ������뻻�з�
                                                                    0x02�����Զ����У���ʾ���ݲ���Ҫ���з�������ֻ��ʹ��ͳһ�����������Ӣ������
        DisplayMode              1                                ��ʾ��ʽ���䶨�����£�
                                                                    0x01������ֹ��ʾ
                                                                    0x02�������ٴ��
                                                                    0x03���������ƶ�
                                                                    0x04���������ƶ�
                                                                    0x05���������ƶ�
                                                                    0x06���������ƶ�
      ExitMode                   1                  0x00          �˳���ʽ
      Speed                      1                                ��ʾ�ٶȣ��������£�
                                                                    0x00�������
                                                                    0x01����
                                                                      ����
                                                                    0x17����
                                                                    0x18 ��������
      StayTime                   1                                ��ʾ�ؼ�ͣ��ʱ�䣬��λΪ 0.5s
      TextLen                                                     ��ʾ���򳤶�
      String AreaText                                             ��ʾ��������
		 * 
		 */
	public native int SCREEN_SendDynamicArea(int dwHand, short AreaX,
			short AreaY, short AreaWidth, short AreaHeight,
			byte DynamicAreaLoc, byte Lines_sizes, byte RunMode, short Timeout,
			byte SingleLine, byte NewLine, byte DisplayMode, byte ExitMode,
			byte Speed, byte StayTime,String AreaText);
	
	public native int SCREEN_Test(int dwHand, byte TestTime);
	//ȡ�����ǿ��ػ�
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
	//����IP��ַ
	public native int BX5MK_SetIPAddress(int dwHand, byte ConnnectMode,
			String ip, String SubnetMask, String Gateway, short port,
			byte ServerMode, String ServerIPAddress, short ServerPort,
			String ServerAccessPassword, short HeartBeatInterval, String NetID);
	//����MAC��ַ
	public native int BX5MK_SetMACAddress(int dwHand, byte[] MAC);

	public native int BX5MK_WebSearch(int dwHand, byte[] Status, byte[] Error,
			String IP, String SubNetMask, String Gate, byte[] Port, String Mac,
			String NetID);

	public native int BX5MK_DelPageData(int dwHand, byte PageLog);
	//��ʽ���ļ�ϵͳ
	public native int OFS_Formatting(int dwHand);
	//ɾ����Ŀ
	public native int OFS_DeleteFile(int dwHand, String pFileName);
	//��ʼ���Ͷ����Ŀ
	public native int OFS_BeginSendMultiFiles(int dwHand);
	//���ͽ�Ŀ�ļ�
	public native int OFS_SendFile(int dwHand, byte overwrite, String pFilePath);
	//���ͽ�Ŀ����
	/*
	  		���� 				���ݳ��� 			Ĭ��ֵ 				����
	  	 pFileName 			  4 							PXXX �ļ���XXX Ϊ�ļ���ţ� ASCII ���ʾ
													           	�ļ���Ϊ�ַ��������Ͱ�˳���ͣ��硰 P123�����ȷ��͡� P����
													           	����͡� 3�������� LOGO �ļ���Ϊ�� LOGO����FileLen 4 �ļ�����
	  DisplayType 			  2 							��Ŀ���ŷ�ʽ
	  														0����˳�򲥷ţ����������������ŵ�ʱ�䣬��λΪ��
	  PlayTimes			  	  1 							��Ŀ�ظ����Ŵ���
	  ProgramLife			  8 							��Ŀ�������ڣ�����˳��Ϊ��StartYear,StartMonth,StartDay,EndYear,EndMonth,EndDay
																��ʼ��(2)+��ʼ��(1)+��ʼ��(1)+ ������(2)+������(1)+������(1)
																ע��
																1. ʱ������� BCD ��ķ�ʽ
																2. �귶ΧΪ 0x1900��0x2099�� 0xffff Ϊ������Ч���ȷ��� LSB������ MSB
	 ProgramWeek 			  1 							��Ŀ����������
	 															1. Bit0 Ϊ 1 ��ʾһ���е�ÿһ�춼���š�
	 															2. Bit0 Ϊ 0 ʱ�����ж� bit1-bit7 ��������ÿ�첥�ţ� bit1-bit7 ���α�ʾ��һ�����ա�
	 															3. ����Ϊ 0 ��ʾ��ֹ���ţ�Ϊ 1 ��ʾ���š�
	 IsPlayOnTime 			  1								 1��ʱ��Ŀλ   0 �Ƕ�ʱע��Ϊ 0 ʱ�򲥷�ʱ������Ĭ��ֵΪ 0 
	 Periodlist                                              ��Ŀ�����б� 
	 Arealist												   ��Ŀ��������
	 */
	
	public native  int OFS_SendFileData(int dwHand, byte overwrite, String pFileName, 
			short DisplayType, byte PlayTimes, boolean IsValidAlways,
			short StartYear,byte StartMonth,byte StartDay,short EndYear,byte EndMonth,byte EndDay,
			byte ProgramWeek, boolean IsPlayOnTime, byte[] Periodlist, Led5kArea[] Arealist);
	//�������Ͷ����Ŀ
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
		/* ���Ͷ�̬����
		led.SCREEN_SendDynamicArea(hand,(short)0,(short)0,(short)128,(short)52,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG1");	
		
		led.SCREEN_SendDynamicArea(hand,(short)128,(short)0,(short)128,(short)52,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG2");
		
		led.SCREEN_SendDynamicArea(hand,(short)256,(short)0,(short)128,(short)52,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG3");
		
		led.SCREEN_SendDynamicArea(hand,(short)0,(short)52,(short)384,(short)12,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x02,"YANGBANG4");
		
		led.SCREEN_SendDynamicArea(hand,(short)0,(short)0,(short)64,(short)32,(byte)1,(byte)0,(byte)0,
				(short)2,(byte)0x02,(byte)0x02,(byte)0x03,(byte)0x00,(byte)0x02,(byte)0x00,"�Ϻ���������Ƽ����޹�˾");
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
		Arealist[0].text="���ͽ�Ŀ";
		//���ͽ�Ŀ
		led.OFS_SendFileData(hand, overwrite, "P001", (short)0, (byte)2, true, (short)0, 
				(byte)0, (byte)0,(short)0, (byte)0, (byte)0, (byte)1, true, Periodlist, 
				Arealist);
		led.OFS_EndSendMultiFiles(hand);
		led.Destroy(hand);
		led.ReleaseSdk();
	}
}
