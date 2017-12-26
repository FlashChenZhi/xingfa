package com.asrs.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Zhouyue
 * Date: 2010-7-19
 * Time: 17:17:35
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "ASRSJOBLOG")
public class AsrsJobLog
{
      private int _id;

      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
      @SequenceGenerator(name = "seq", sequenceName = "SEQ_ASRSJOBLOG_ID", allocationSize = 1)
      @Column(name = "ID", nullable = false, length = 8)
      public int getId()
      {
            return _id;
      }

      public void setId(int id)
      {
            _id = id;
      }

      private int _jobID;

      @Column(name = "JOBID")
      @Basic
      public int getJobID()
      {
            return _jobID;
      }

      public void setJobID(int jobID)
      {
            _jobID = jobID;
      }

      private String _jobType;

      private String _mcKey;

      @Column(name = "MCKEY")
      @Basic
      public String getMcKey()
      {
            return _mcKey;
      }

      public void setMcKey(String mcKey)
      {
            _mcKey = mcKey;
      }

      private String _fromStation;

      @Column(name = "FROMSTATION")
      @Basic
      public String getFromStation()
      {
            return _fromStation;
      }

      public void setFromStation(String fromStation)
      {
            _fromStation = fromStation;
      }

      private String _fromProxyStation;

      @Column(name = "FROMPROXYSTATION")
      @Basic
      public String getFromProxyStation()
      {
            return _fromProxyStation;
      }

      public void setFromProxyStation(String fromProxyStation)
      {
            _fromProxyStation = fromProxyStation;
      }

      private String _toProxyStation;

      @Column(name = "TOPROXYSTATION")
      @Basic
      public String getToProxyStation()
      {
            return _toProxyStation;
      }

      public void setToProxyStation(String toProxyStation)
      {
            _toProxyStation = toProxyStation;
      }

      private String _toStation;

      @Column(name = "TOSTATION")
      @Basic
      public String getToStation()
      {
            return _toStation == null ? "" : _toStation;
      }

      public void setToStation(String toStation)
      {
            _toStation = toStation;
      }

      private String _fromLocation;

      @Column(name = "FROMLOCATION")
      @Basic
      public String getFromLocation()
      {
            return _fromLocation;
      }

      public void setFromLocation(String fromLocation)
      {
            _fromLocation = fromLocation;
      }

      private String _barcode;

      @Column(name = "BARCODE")
      @Basic
      public String getBarcode()
      {
            return _barcode == null ? "" : _barcode;
      }

      public void setBarcode(String barcode)
      {
            _barcode = barcode;
      }

      private String _toLocation;

      @Column(name = "TOLOCATION")
      @Basic
      public String getToLocation()
      {
            return _toLocation;
      }

      public void setToLocation(String toLocation)
      {
            _toLocation = toLocation;
      }


      private String _type;

      @Column(name = "TYPE")
      @Basic
      public String getType()
      {
            return _type;
      }

      public void setType(String type)
      {
            _type = type;
      }

      private String _status;

      @Column(name = "STATUS")
      @Basic
      public String getStatus()
      {
            return _status;
      }

      public void setStatus(String status)
      {
            _status = status;
      }

      private String _statusDetail;

      @Column(name = "STATUSDETAIL")
      @Basic
      public String getStatusDetail()
      {
            return _statusDetail;
      }

      public void setStatusDetail(String statusDetail)
      {
            _statusDetail = statusDetail;
      }

      private int _priority;

      @Column(name = "PRIORITY")
      @Basic
      public int getPriority()
      {
            return _priority;
      }

      public void setPriority(int priority)
      {
            _priority = priority;
      }

      private Date _generateTime;

      @Column(name = "GENERATETIME")
      @Basic
      public Date getGenerateTime()
      {
            return _generateTime;
      }

      public void setGenerateTime(Date generateTime)
      {
            _generateTime = generateTime;
      }

      private Date _startTime;

      @Column(name = "STARTTIME")
      @Basic
      public Date getStartTime()
      {
            return _startTime;
      }

      public void setStartTime(Date startTime)
      {
            _startTime = startTime;
      }

      private Date _doneTime;

      @Column(name = "DONETIME")
      @Basic
      public Date getDoneTime()
      {
            return _doneTime;
      }

      public void setDoneTime(Date doneTime)
      {
            _doneTime = doneTime;
      }

      private String _plcName;

      @Column(name = "PLCNAME")
      @Basic
      public String getPlcName()
      {
            return _plcName;
      }

      public void setPlcName(String plcName)
      {
            _plcName = plcName;
      }

      private String _storageType;

      @Column(name = "STORAGETYPE")
      @Basic
      public String getStorageType()
      {
            return _storageType;
      }

      public void setStorageType(String storageType)
      {
            _storageType = storageType;
      }

      private String _reInput;

      @Column(name = "REINPUT")
      @Basic
      public String getReInput()
      {
            return _reInput;
      }

      public void setReInput(String reInput)
      {
            _reInput = reInput;
      }

      private Boolean _indicating;

      @Column(name = "INDICATING")
      @Basic
      public Boolean getIndicating()
      {
            return _indicating;
      }

      public void setIndicating(Boolean indicating)
      {
            _indicating = indicating;
      }

      private String _controlInfo;

      @Column(name = "CONTROLINFO")
      @Basic
      public String getControlInfo()
      {
            return _controlInfo;
      }

      public void setControlInfo(String controlInfo)
      {
            _controlInfo = controlInfo;
      }

      private String _retrievalType;


      @Column(name = "RETRIEVALTYPE")
      @Basic
      public String getRetrievalType()
      {
            return _retrievalType;
      }

      public void setRetrievalType(String retrievalType)
      {
            _retrievalType = retrievalType;
      }

      @Column(name = "JOBTYPE")
      @Basic
      public String getJobType()
      {
            return _jobType;
      }

      public void setJobType(String jobType)
      {
            _jobType = jobType;
      }


      private String _toArea;

      @Column(name = "TOAREA")
      @Basic
      public String getToArea()
      {
            return _toArea;
      }

      public void setToArea(String toArea)
      {
            _toArea = toArea;
      }

      private String _errorMsg;

      @Column(name = "ERRORMSG")
      @Basic
      public String getErrorMsg()
      {
            return _errorMsg;
      }

      public void setErrorMsg(String errorMsg)
      {
            _errorMsg = errorMsg;
      }

      @Override
      public boolean equals(Object o)
      {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AsrsJobLog that = (AsrsJobLog) o;

            if (_id != that._id) return false;
            if (_jobID != that._jobID) return false;
            if (_priority != that._priority) return false;
            if (_plcName != null ? !_plcName.equals(that._plcName) : that._plcName != null) return false;
            if (_barcode != null ? !_barcode.equals(that._barcode) : that._barcode != null) return false;
            if (_controlInfo != null ? !_controlInfo.equals(that._controlInfo) : that._controlInfo != null) return false;
            if (_doneTime != null ? !_doneTime.equals(that._doneTime) : that._doneTime != null) return false;
            if (_fromLocation != null ? !_fromLocation.equals(that._fromLocation) : that._fromLocation != null) return false;
            if (_fromProxyStation != null ? !_fromProxyStation.equals(that._fromProxyStation) : that._fromProxyStation != null) return false;
            if (_fromStation != null ? !_fromStation.equals(that._fromStation) : that._fromStation != null) return false;
            if (_generateTime != null ? !_generateTime.equals(that._generateTime) : that._generateTime != null) return false;
            if (_indicating != null ? !_indicating.equals(that._indicating) : that._indicating != null) return false;
            if (_jobType != null ? !_jobType.equals(that._jobType) : that._jobType != null) return false;
            if (_mcKey != null ? !_mcKey.equals(that._mcKey) : that._mcKey != null) return false;
            if (_reInput != null ? !_reInput.equals(that._reInput) : that._reInput != null) return false;
            if (_retrievalType != null ? !_retrievalType.equals(that._retrievalType) : that._retrievalType != null) return false;
            if (_startTime != null ? !_startTime.equals(that._startTime) : that._startTime != null) return false;
            if (_status != null ? !_status.equals(that._status) : that._status != null) return false;
            if (_statusDetail != null ? !_statusDetail.equals(that._statusDetail) : that._statusDetail != null) return false;
            if (_storageType != null ? !_storageType.equals(that._storageType) : that._storageType != null) return false;
            if (_toArea != null ? !_toArea.equals(that._toArea) : that._toArea != null) return false;
            if (_toLocation != null ? !_toLocation.equals(that._toLocation) : that._toLocation != null) return false;
            if (_toProxyStation != null ? !_toProxyStation.equals(that._toProxyStation) : that._toProxyStation != null) return false;
            if (_toStation != null ? !_toStation.equals(that._toStation) : that._toStation != null) return false;
            if (_type != null ? !_type.equals(that._type) : that._type != null) return false;

            return true;
      }

      @Override
      public int hashCode()
      {
            int result = _id;
            result = 31 * result + _jobID;
            result = 31 * result + (_jobType != null ? _jobType.hashCode() : 0);
            result = 31 * result + (_mcKey != null ? _mcKey.hashCode() : 0);
            result = 31 * result + (_fromStation != null ? _fromStation.hashCode() : 0);
            result = 31 * result + (_fromProxyStation != null ? _fromProxyStation.hashCode() : 0);
            result = 31 * result + (_toProxyStation != null ? _toProxyStation.hashCode() : 0);
            result = 31 * result + (_toStation != null ? _toStation.hashCode() : 0);
            result = 31 * result + (_fromLocation != null ? _fromLocation.hashCode() : 0);
            result = 31 * result + (_barcode != null ? _barcode.hashCode() : 0);
            result = 31 * result + (_toLocation != null ? _toLocation.hashCode() : 0);
            result = 31 * result + (_type != null ? _type.hashCode() : 0);
            result = 31 * result + (_status != null ? _status.hashCode() : 0);
            result = 31 * result + (_statusDetail != null ? _statusDetail.hashCode() : 0);
            result = 31 * result + _priority;
            result = 31 * result + (_generateTime != null ? _generateTime.hashCode() : 0);
            result = 31 * result + (_startTime != null ? _startTime.hashCode() : 0);
            result = 31 * result + (_doneTime != null ? _doneTime.hashCode() : 0);
            result = 31 * result + (_plcName != null ? _plcName.hashCode() : 0);
            result = 31 * result + (_storageType != null ? _storageType.hashCode() : 0);
            result = 31 * result + (_reInput != null ? _reInput.hashCode() : 0);
            result = 31 * result + (_indicating != null ? _indicating.hashCode() : 0);
            result = 31 * result + (_controlInfo != null ? _controlInfo.hashCode() : 0);
            result = 31 * result + (_retrievalType != null ? _retrievalType.hashCode() : 0);
            result = 31 * result + (_toArea != null ? _toArea.hashCode() : 0);
            return result;
      }
}
