import {Button,Modal, Popover,Tabs ,Form, Radio, Table, Badge, DatePicker, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
import './css/jquery.seat-charts.css';
import './js/jquery-1.9.1';
import './js/jquery.seat-charts';
import PopoverModel from './PopoverModel';


const TabPane = Tabs.TabPane;
const Option = Select.Option;
const confirm = Modal.confirm;
const RangePicker = DatePicker.RangePicker;
const RadioGroup = Radio.Group;

var sc ="";


let OutputArea = React.createClass({
    getInitialState(){
        return {
            commodityCodeList:[],
            commodityCodeFirst:"",
            map:[],
            availableList:[],
            reservedOutList:[],
            reservedInList:[],
            emptyList:[],
            tabKey:1,
            PopoverModelVisible:false,
            focusLocation:"",
            msg:"",
            skuName:"",
            skuCode:"",
            lotNum:"",
            barcode:"",
            qty:"",
            selectLocation:[],
            cancelLocation:[],
        };
    },
    componentDidMount(){
        this.getCommodityCode();
        this.getStorageLocationData(1);
        this.initChart();
    },

    getCommodityCode(){
        reqwest({
            url: '/wms/master/FindOutOrInWarehouseAction/getSkuCode',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success) {
                    this.setState({
                        commodityCodeList: json.res,
                        commodityCodeFirst: json.res[0],
                    })
                }else{
                    message.error("初始化Sku代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化Sku代码失败！");
            }.bind(this)
        })
    },
    getStorageLocationData(level){
        const values = this.props.form.getFieldsValue();
        console.log(values);
        reqwest({
            url: '/wms/master/SampleSurveyAction/getStorageLocationData',
            dataType: 'json',
            method: 'post',
            data: {productId:values.productId,tier:level},
            success: function (json) {
                if(json.success) {
                    console.log(json.res);
                    this.setState({
                        map: json.res.map,
                        availableList: json.res.availableList,
                        emptyList: json.res.emptyList,
                        reservedOutList:json.res.reservedOutList,
                        reservedInList:json.res.reservedInList,
                        selectLocation:[],
                    })
                    console.log(this.state.map);
                    $(".legend").hide();
                    if(level==1){
                        $("#legend1").show();
                        this.initChart(this.state.map,"#seat-map1","#legend1");
                    }else if(level==2){
                        $("#legend2").show();
                        this.initChart(this.state.map,"#seat-map2","#legend2");
                    }else if(level==3){
                        $("#legend3").show();
                        this.initChart(this.state.map,"#seat-map3","#legend3");
                    }else if(level==4){
                        $("#legend4").show();
                        this.initChart(this.state.map,"#seat-map4","#legend4");
                    }
                    sc.get(json.res.unavailableList).status('unavailable');
                    sc.get(json.res.availableList).status('available');
                    sc.get(json.res.unavailableList1).status('unavailable');
                    sc.get(json.res.unavailableList2).status('unavailable');
                    sc.get(json.res.reservedOutList).status('reservedOut');
                    sc.get(json.res.emptyList).status('empty');
                    sc.get(json.res.reservedInList).status('reservedIn');
                    sc.get(json.res.unavailableList3).status('reservedCheck');
                    sc.get(json.res.unavailableList4).status('reservedCheck');
                }else{
                    message.error("初始化库位代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化库位代码失败！");
            }.bind(this)
        })
    },

    initChart(map,divId,legendId) {
        var thisOut = this;
        var $cart = $('#selected-seats'), //库位
            $counter = $('#counter'), //票数
            $total = $('#total'); //总计金额


         sc = $(divId).seatCharts({
            map: map,
            naming:{
                top    : true,
                left   : true,

                getId  : function(character, row, column) {
                    return row + '_' + column;
                },
                getLabel : function (character, row, column) {
                    return column;
                },
                getData:function(dataList){
                    console.log(dataList)
                    return dataList;
                },
                data: map,
            },
            legend : { //定义图例
                node : $(legendId),
                items : [
                    [ 'a', 'available', '可选货位' ],
                    [ 'a', 'unavailable', '不可选货位'],
                    [ 'a', 'reservedOut', '已有出库任务'],
                    [ 'a', 'reservedIn', '已有入库任务'],
                    [ 'a', 'empty', '空货位'],
                    [ 'a', 'selected', '已选择货位'],
                    [ 'a', 'reservedCheck', '已有抽检任务'],
                ]
            },
            click: function () { //点击事件
                if (this.status() == 'available') {
                    $('<li>'+(this.settings.row+1)+'排'+this.settings.label+'座</li>')
                        .attr('id', 'cart-item-'+this.settings.id)
                        .data('seatId', this.settings.id)
                        .appendTo($cart);
                    $counter.text(sc.find('selected').length+1);
                    thisOut.outClick(this.settings);
                    return 'selected';
                } else if (this.status() == 'selected') {
                    thisOut.outClick(this.settings);
                    $counter.text(sc.find('selected').length-1);
                    //删除已预订座位
                    $('#cart-item-'+this.settings.id).remove();
                    //可选座
                    return 'available';
                } else if (this.status() == 'unavailable') {
                    return 'unavailable';
                } else {
                    return this.style();
                }
            },
            focus  : function() {
                 thisOut.outFocus(this.settings);

                 if (this.status() == 'available') {
                     return 'focused';
                 } else  {
                     return this.style();
                 }
             },
             blur   : function() {
                 thisOut.outBlur(this.settings);

                 return this.status();
             },
        });
        console.log(sc.data);
        //已售出的座位

    },
    hideChangeLevelModel() {
        this.setState({blockNo: null, changeLevelModel: false});
    },
    outClick(settings){
        let bank = settings.row+1;
        let bay = settings.column+1;
        let level = this.state.tabKey;

        if(settings.status=='available'){
            this.state.selectLocation.forEach((s)=>{
                sc.get(s).status('available');
                let index = this.state.selectLocation.indexOf(s);
                if(index >= 0){
                    this.state.selectLocation.splice(index,1);
                }
            });
            this.state.selectLocation.push(bank+'_'+bay);
            console.log( this.state.selectLocation);

        }else if(settings.status=='selected'){
            this.state.selectLocation.forEach((s)=>{
                sc.get(s).status('available');
                let index = this.state.selectLocation.indexOf(s);
                if(index >= 0){
                    this.state.selectLocation.splice(index,1);
                }
            });
            console.log( this.state.selectLocation);
        }

    },
    outFocus(settings){
        let bank = settings.row+1;
        let bay = settings.column+1;
        let level = this.state.tabKey;
        this.setState({PopoverModelVisible: true});
        reqwest({
            url: '/wms/master/SampleSurveyAction/getLocationInfo',
            dataType: 'json',
            method: 'post',
            data: {bank:bank,bay:bay,level:level},
            success: function (json) {
                if(json.success) {

                    this.setState({
                        skuName:json.res.skuName,
                        skuCode:json.res.skuCode,
                        lotNum:json.res.lotNum,
                        qty:json.res.qty,
                        msg:json.res.msg,
                        barcode:json.res.barcode,
                    });
                    if(json.res.bank!=null){
                        this.setState({
                            locationInfo:json.res.bank+'排'+json.res.bay+'列'+json.res.level+'层',
                        })
                    }else{
                        this.setState({
                            locationInfo:"",
                        })
                    }
                }else{
                    message.error("初始化库位代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化库位代码失败！");
            }.bind(this)
        })
    },
    outBlur(settings){

        this.setState({focusLocation:"",PopoverModelVisible: false});
    },
    PrefixInteger(num, length) {
        return ( "000" + num ).substr( -length );
    },
    handleSubmit2(e) {
        let locationList =JSON.stringify(this.state.selectLocation);
        let level = this.state.tabKey;
        reqwest({
            url: '/wms/master/SampleSurveyAction/assignsTheStorehouse',
            dataType: 'json',
            method: 'post',
            data: { selectLocation:locationList,level:level},
            success: function (json) {
                if(json.success) {
                    message.success(json.msg);
                    let level = this.state.tabKey;
                    this.getStorageLocationData(level);
                }else{
                    message.error(json.msg);
                    //this.getStorageLocationData(level);
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化库位代码失败！");
            }.bind(this)
        })
    },
    handleSubmit(e) {
        e.preventDefault();
        let level = this.state.tabKey;
        this.getStorageLocationData(level);
    },

    handleReset(e) {
        this.props.form.resetFields();
        window.location.reload();
        /*for(let i =1;i<5;i++){
            this.getStorageLocationData(i);
        }*/
    },
    tabCallback(key){
        console.log(key);
        this.setState({
            tabKey:key,
            selectLocation:[],
        });
        this.getStorageLocationData(key);
    },

    render() {

        const {getFieldProps} = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const formItemLayout2 = {
            labelCol: {span: 5},
            wrapperCol: {span: 10},

        };
        const commodityCodeProps = getFieldProps('productId', {
            initialValue:"",
        });
        const tierProps = getFieldProps('tier', {
            initialValue:"1",
        });
        const commodityCodeListSelect =[];
        commodityCodeListSelect.push(<Option value="">---请选择---</Option>);
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.skuCode}>{commodityCode.skuName}</Option>);
        });
        return (
            <div style={{overflow:"auto",width:"1600px"}}>
                <Form horizontal>
                    <Row>
                        <Col lg={9}>
                            <FormItem
                                {...formItemLayout}
                                label="商品名称："
                            >
                                <Select
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    id="select" size="large" style={{ width: 200 }}
                                    {...commodityCodeProps} >
                                    {commodityCodeListSelect}
                                </Select>
                            </FormItem>

                            <FormItem wrapperCol={{offset: 10}}>
                                <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.handleReset}>重置</Button>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <Button type="primary" onClick={this.handleSubmit2}>提交</Button>
                            </FormItem>

                        </Col>


                        <Col lg={15}>
                            <br/><br/><br/>
                            <div id="legend1" className={'legend'}></div>
                            <div id="legend2" className={'legend'}></div>
                            <div id="legend3" className={'legend'}></div>
                            <div id="legend4" className={'legend'}></div>

                        </Col>
                        <Col lg={3}>
                            <div id="Info1" style={{paddingLeft:"30px",fontWeight: "bold"}}>
                                <span >货位状态：</span>{this.state.msg}<br/>
                                <span >位置信息：</span>{this.state.locationInfo}<br/>
                                <span>商品数量：</span>{this.state.qty}

                            </div>
                        </Col>
                        <Col lg={10}>
                            <div id="Info1" style={{paddingLeft:"10px",fontWeight: "bold"}}>
                                <span >商品名称：</span>{this.state.skuName}<br/>
                                <span >托盘号码：</span>{this.state.barcode}<br/>
                                <span >商品批次：</span>{this.state.lotNum}
                            </div>
                        </Col>
                    </Row>
                </Form>
                <Row>
                    <Tabs defaultActiveKey="1" onChange={this.tabCallback}>
                        <TabPane tab="第一层" key="1">
                            <div id="seat-map1"  >
                                <div className={'front'}>第一层库位</div>
                            </div>
                        </TabPane>
                        <TabPane tab="第二层" key="2">
                            <div id="seat-map2"  >
                                <div className={'front'}>第二层库位</div>
                            </div>
                        </TabPane>
                        <TabPane tab="第三层" key="3">
                            <div id="seat-map3"  >
                                <div className={'front'}>第三层库位</div>
                            </div>
                        </TabPane>
                        <TabPane tab="第四层" key="4">
                            <div id="seat-map4"  >
                                <div className={'front'}>第四层库位</div>
                            </div>
                        </TabPane>
                    </Tabs>
                </Row>
                {/*<PopoverModel*/}
                    {/*code={this.state.blockNo}*/}
                    {/*visible={this.state.PopoverModelVisible}*/}
                    {/*hideModel={this.hideChangeLevelModel.bind(this)}*/}
                {/*/>*/}
            </div>
        );
    },
});
OutputArea = Form.create({})(OutputArea);
export default OutputArea;
