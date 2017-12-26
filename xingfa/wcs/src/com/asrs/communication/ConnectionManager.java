package com.asrs.communication;

import com.util.common.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-3
 * Time: 16:41:59
 * Copy right Worgsoft.
 */

public class ConnectionManager implements Runnable
{
      private int Interval = 5000;

      public boolean isAutoConnect()
      {
            return isAutoConnect;
      }

      public void setAutoConnect(boolean autoConnect)
      {
            isAutoConnect = autoConnect;
      }

      private boolean isAutoConnect = true;

      HashMap<String, PlcConnection> _connectionPool = new HashMap<String, PlcConnection>();

      public ConnectionManager()
      {
//        _connCheckThread.start();
      }

      public void Connect(String name, String ip, int port)
      {
            PlcConnection conn = null;
            if (_connectionPool.containsKey(name))
            {
                  conn = _connectionPool.get(name);
            }
            else
            {
                  conn = new PlcConnection(name, ip, port);
                  _connectionPool.put(name, conn);
            }

            if (!conn.IsConnected())
            {
                  try
                  {
                        conn.connect();
                        System.out.println(conn.getPlcName() + " Connected!");
                  }
                  catch (CommunicationException e)
                  {
                        String errMsg = "无法连接" + conn.getPlcName();
                        System.out.println(errMsg);
                        LogWriter.writeError(this.getClass(), errMsg);
                  }
            }

      }

      public void DisConnect(String name) throws CommunicationException {
            if (_connectionPool.containsKey(name))
            {
                  PlcConnection conn = _connectionPool.get(name);
                  _connectionPool.remove(name);
                  conn.disconnect();
            }
      }

      public void Send(String plcName, byte[] data) throws IOException, CommunicationException
      {
            if (_connectionPool.containsKey(plcName))
            {
                  PlcConnection conn = _connectionPool.get(plcName);
                  conn.send(data);
            }
      }

      public void Send(String plcName, String data) throws IOException, CommunicationException
      {
            if (_connectionPool.containsKey(plcName))
            {
                  PlcConnection conn = _connectionPool.get(plcName);
                  conn.send(data);
            }
      }


      public void run()
      {
            while (true)
            {
                  try
                  {
                        Thread.sleep(Interval);
                  }
                  catch (InterruptedException e)
                  {
                        e.printStackTrace();
                        LogWriter.writeError(this.getClass(), e.getMessage());
                  }
                  if (isAutoConnect)
                  {
                        for (PlcConnection conn : _connectionPool.values())
                        {
                              if (!conn.IsConnected())
                              {
                                    try
                                    {
                                          conn.connect();
                                          System.out.println(conn.getPlcName() + " Connected!");
                                    }
                                    catch (CommunicationException e)
                                    {
                                          String errMsg = "无法连接" + conn.getPlcName();
                                          System.out.println(errMsg);
                                          LogWriter.writeError(this.getClass(), errMsg);
                                    }
                              }
                        }
                  }
            }
      }
}
