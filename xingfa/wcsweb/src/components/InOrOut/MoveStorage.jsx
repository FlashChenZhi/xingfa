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
var content = (
    <div>
        <p>Content</p>
        <p>Content</p>
    </div>
);

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
            fromLocation:[],
            toLocation:[],
            fromLocationNos:[],
            toLocationNos:[],
            selectStatus:1,
        };
    },
    componentDidMount(){
        this.getCommodityCode();
        this.getStorageLocationData(1,1,1,"");
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
    getStorageLocationData(level,status,selectStatus,fromLocationNo){
        const values = this.props.form.getFieldsValue();
        console.log("selectStatus:"+selectStatus);
        console.log("fromLocationNo:"+fromLocationNo);

        reqwest({
            url: '/wms/master/moveStorageAction/getStorageLocationData',
            dataType: 'json',
            method: 'post',
            data: {productId:values.productId,tier:level,selectStatus:selectStatus,fromLocationNo:fromLocationNo},
            success: function (json) {
                if(json.success) {
                    console.log(json.res);
                    this.setState({
                        map: json.res.map,
                        availableList: json.res.availableList,
                        emptyList: json.res.emptyList,
                        reservedOutList:json.res.reservedOutList,
                        reservedInList:json.res.reservedInList,

                    });
                    if(status==1){
                        this.setState({
                            selectLocation:[],
                        })
                    }
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
                    sc.get(json.res.reservedOutList).status('reservedOut');
                    sc.get(json.res.emptyList).status('empty');
                    sc.get(json.res.reservedInList).status('reservedIn');
                    sc.get(json.res.unavailableList1).status('unavailable');
                    sc.get(json.res.unavailableList3).status('reservedCheck');
                    sc.get(json.res.unavailableList4).status('reservedCheck');
                    if(this.state.selectStatus==2){
                        sc.get(json.res.availableList).status('available');
                    }
                    if(this.state.fromLocationNos.length!=0){
                        let lev =this.state.fromLocationNos[0];
                        if(lev==this.state.tabKey){
                            sc.get(this.state.fromLocationNos).status('fromLocation');
                        }
                    }
                    if(this.state.toLocationNos.length!=0){
                        let lev =this.state.toLocationNos[0];
                        if(lev==this.state.tabKey) {
                            sc.get(this.state.toLocationNos).status('toLocation')
                        }
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
                    [ 'a', 'fromLocation', '要移货位'],
                    [ 'a', 'toLocation', '移至货位'],
                ]
            },
            click: function () { //点击事件
                if (this.status() == 'available') {
                    /*$('<li>'+(this.settings.row+1)+'排'+this.settings.label+'座</li>')
                        .attr('id', 'cart-item-'+this.settings.id)
                        .data('seatId', this.settings.id)
                        .appendTo($cart);
                    $counter.text(sc.find('selected').length+1);*/
                    if(thisOut.state.fromLocation.length==0){
                        thisOut.outClick(this.settings);
                    }else{
                        thisOut.outClickk(this.settings);
                    }
                    return 'selected';
                } else if (this.status() == 'selected') {
                    if(thisOut.state.fromLocation.length==0){
                        thisOut.outClick(this.settings);
                    }else{
                        thisOut.outClickk(this.settings);
                    }
                    /*$counter.text(sc.find('selected').length-1);
                    //删除已预订座位
                    $('#cart-item-'+this.settings.id).remove();*/
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
            reqwest({
                url: '/wms/master/moveStorageAction/getNextAvailableLocation',
                dataType: 'json',
                method: 'post',
                data: {bank:bank,bay:bay,level:level},
                success: function (json) {
                    console.log("json res:"+json.res);
                    if(json.success) {
                        if(json.res.status){
                            sc.get(json.res.otherlocation).status('unavailable');
                            sc.get(json.res.location).status('available');
                        }else{
                            sc.get(json.res.otherlocation).status('unavailable');
                        }
                        let locationNo = this.PrefixInteger(bank,3)+this.PrefixInteger(bay,3)+this.PrefixInteger(level,3);
                        this.state.selectLocation.push(locationNo);
                        console.log(this.state.selectLocation);
                    }else{
                        message.error("获取下一位库位代码失败！");
                    }
                }.bind(this),
                error: function (err) {
                    message.error("获取下一位库位代码失败！");
                }.bind(this)
            })
        }else if(settings.status=='selected'){
            if(this.state.selectLocation.length==1){

                this.getStorageLocationData(level,1,this.state.selectStatus,"");
            }else{
                reqwest({
                    url: '/wms/master/moveStorageAction/getAgoUnavailableLocation',
                    dataType: 'json',
                    method: 'post',
                    data: {bank:bank,bay:bay,level:level},
                    success: function (json) {
                        if(json.success) {
                            if(json.res.status){
                                sc.get(json.res.location).status('unavailable');
                            }
                            this.setState({
                                cancelLocation:json.res.location,
                            })

                            this.state.cancelLocation.forEach((s)=>{
                                let locationNo = this.PrefixInteger(s.split("_")[0],3)+this.PrefixInteger(s.split("_")[1],3)+this.PrefixInteger(level,3);
                                let index = this.state.selectLocation.indexOf(locationNo);
                                if(index >= 0){
                                    this.state.selectLocation.splice(index,1);
                                }
                            })
                            let locationNo = this.PrefixInteger(bank,3)+this.PrefixInteger(bay,3)+this.PrefixInteger(level,3);
                            let index = this.state.selectLocation.indexOf(locationNo);
                            if(index >= 0){
                                this.state.selectLocation.splice(index,1);
                            }
                            console.log( this.state.selectLocation);
                        }else{
                            message.error("初始化库位代码失败！");
                        }
                    }.bind(this),
                    error: function (err) {
                        message.error("初始化库位代码失败！");
                    }.bind(this)
                })
            }

        }

    },
    outClickk(settings){
        let bank = settings.row+1;
        let bay = settings.column+1;
        let level = this.state.tabKey;
        if(settings.status=='available'){
            let locationNo = this.PrefixInteger(bank,3)+this.PrefixInteger(bay,3)+this.PrefixInteger(level,3);
            this.state.selectLocation.push(locationNo);
            console.log(this.state.selectLocation);

        }else if(settings.status=='selected'){
            console.log( this.state.selectLocation);
            let locationNo = this.PrefixInteger(bank,3)+this.PrefixInteger(bay,3)+this.PrefixInteger(level,3);
            let index = this.state.selectLocation.indexOf(locationNo);
            if(index >= 0){
                this.state.selectLocation.splice(index,1);
            }
            console.log( this.state.selectLocation);

        }
    },
    outFocus(settings){
        let bank = settings.row+1;
        let bay = settings.column+1;
        let level = this.state.tabKey;
        this.setState({PopoverModelVisible: true});
        reqwest({
            url: '/wms/master/moveStorageAction/getLocationInfo',
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
        console.log("from:"+this.state.fromLocation+",to:"+this.state.toLocation+",select:"+this.state.selectLocation);
        if(this.state.fromLocation.length==0){
            message.error("请先选择要移货位！");
        }else if(this.state.toLocation.length==0){
            message.error("请先选择到达货位！");
        }else if(this.state.fromLocation.length!=this.state.toLocation.length){
            message.error("已选到达货位和已选要移货位数量不一致！");
        }else{
            let fromLocationNos = JSON.stringify(this.state.fromLocation);
            let toLocationNos = JSON.stringify(this.state.toLocation);
            console.log("from:"+fromLocationNos+",to:"+toLocationNos+",select:"+this.state.selectLocation);
            reqwest({
                url: '/wms/master/moveStorageAction/assignsTheStorehouse',
                dataType: 'json',
                method: 'post',
                data: {fromLocationNos:fromLocationNos,toLocationNos:toLocationNos},
                success: function (json) {
                    if(json.success) {
                        message.success(json.msg);
                        let level = this.state.tabKey;
                        this.setState({
                            selectLocation:[],
                            cancelLocation:[],
                            fromLocation:[],
                            toLocation:[],
                            fromLocationNos:[],
                            toLocationNos:[],
                        })
                        this.getStorageLocationData(level,1,1,"");
                    }else{
                        message.error(json.msg);
                    }
                }.bind(this),
                error: function (err) {
                    message.error("初始化库位代码失败！");
                }.bind(this)
            })
        }

    },
    handleSubmit(e) {
        e.preventDefault();
        let level = this.state.tabKey;
        if(this.state.fromLocation.length==0){
            this.getStorageLocationData(level,1,this.state.selectStatus,"");
        }else{
            message.error("已选择要移货位，请重置后查询！");
        }

    },
    fromLocationSubmit(e) {
        e.preventDefault();

        if(this.state.fromLocation.length!=0){
            message.error("已选过要移货位，如需重新选择请重置！");
        }else if(this.state.selectLocation.length==0){
            message.error("所选货位数量不能为0！");
        }else {
            const location = this.state.selectLocation;

            this.setState({
                fromLocation: location,
                selectLocation: [],
            });
            let fromLocationNos=[];
            fromLocationNos.push(this.state.tabKey);
            location.forEach((l) => {
                let bank = l.substr(0, 3).replace(/\b(0+)/gi, "");
                let bay = l.substr(3, 3).replace(/\b(0+)/gi, "");
                let locationNo = bank + "_" + bay;
                fromLocationNos.push(locationNo);
                sc.get(bank + "_" + bay).status('fromLocation');
            });
            if(fromLocationNos.length>1){
                this.setState({
                    fromLocationNos:fromLocationNos,
                })
            }
            this.setState({
                selectStatus:2,
            })
            let fromLocationNo = location[0];
            this.getStorageLocationData(this.state.tabKey,1,2,fromLocationNo);
        }

    },
    toLocationSubmit(e) {
        e.preventDefault();
        if(this.state.fromLocation.length==0){
            message.error("请先选择要移货位！");
        }else if(this.state.fromLocation.length!=this.state.selectLocation.length){
            message.error("已选到达货位和已选要移货位数量不一致！");
        }else if(this.state.selectLocation.length==0){
            message.error("所选货位数量不能为0！");
        }else if(this.state.toLocation.length!=0){
            message.error("已选过到达货位，如需重新选择请重置！");
        }else{
            const location =this.state.selectLocation;
            this.setState({
                toLocation:location,
                selectLocation:[],
            });
            let toLocationNos=[];
            toLocationNos.push(this.state.tabKey);
            location.forEach((l)=>{
                let bank = l.substr(0,3).replace(/\b(0+)/gi,"");
                let bay = l.substr(3,3).replace(/\b(0+)/gi,"");
                let locationNo = bank + "_" + bay;
                toLocationNos.push(locationNo);
                sc.get(bank+"_"+bay).status('toLocation');
            });
            if(toLocationNos.length>1){
                this.setState({
                    toLocationNos:toLocationNos,
                })
            }

        }

    },

    handleReset(e) {
        this.props.form.resetFields();
        window.location.reload();
        /*for(let i =1;i<5;i++){
            this.getStorageLocationData(i,1);
        }*/
    },
    tabCallback(key){
        console.log(key);
        this.setState({
            tabKey:key,
        });
        if(this.state.fromLocation.length!=0){
            let fromLocationNo = this.state.fromLocation[0];
            this.getStorageLocationData(key,1,this.state.selectStatus,fromLocationNo);
        }else{
            this.getStorageLocationData(key,1,this.state.selectStatus,"");
        }

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

                            <FormItem wrapperCol={{offset: 3}}>
                                <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.handleReset}>重置</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.fromLocationSubmit}>要移货位</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.toLocationSubmit}>移至货位</Button>
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
