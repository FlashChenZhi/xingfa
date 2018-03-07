import {Button, Form, Input, Table, Badge, DatePicker, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;

const columns2 = [{
    title: '托盘号',
    dataIndex: 'id',
},{
    title: '托盘号',
    dataIndex: 'containerId',
}, {
    title: '货品代码',
    dataIndex: 'skuCode',
}, {
    title: '货品名称',
    dataIndex: 'skuName',
}, {
    title: '货品数量',
    dataIndex: 'qty',
}, {
    title: '所在行',
    dataIndex: 'bank',
}, {
    title: '所在列',
    dataIndex: 'bay',
}, {
    title: '所在层',
    dataIndex: 'level',
}, {
    title: '入库时间',
    dataIndex: 'dateTime',
}];
let OutputArea = React.createClass({
    getInitialState(){
        return {
            data: [],//表格数据
            data2: [],//表格数据
            data3: [],//表格数据
            data4: [],//表格数据
            data5: [],//表格数据
            data6: [],//表格数据
            total: 0,//表格数据总行数
            total2: 0,//表格数据总行数
            total3: 0,//表格数据总行数
            total4: 0,//表格数据总行数
            total5: 0,//表格数据总行数
            total6: 0,//表格数据总行数
            selectedData: [],//点击设定提交到后台的数据
            loading: false,
            loading2: false,
            loading3: false,
            loading4: false,
            loading5: false,
            loading6: false,
            selectedRowKeys: [],
            defaultPageSize:10,
            defaultPageSize2:5,
            commodityCodeList:[],
            commodityCodeFirst:"",
            shipperIdList:[],
            shipperIdFirst:"",
            current:1,
            current2:1,
            current3:1,
            current4:1,
            current5:1,
            current6:1,
            skuCodeTable2:"",
            skuCodeTable3:"",
            skuCodeTable4:"",
            skuCodeTable5:"",
            skuCodeTable6:"",
        };
    },
    componentDidMount(){
        this.getData(1);
        this.getCommodityCode();
        this.getshipperId();
    },
    createTable2(rowIndex){
        console.log(rowIndex);
        if(rowIndex==1){
            return(
                <Table
                    loading={this.state.loading2}
                    columns={columns2}
                    dataSource={this.state.data2}
                    pagination={false}
                    rowKey={record => record.id}
                    pagination={{
                        onChange: this.pageChange2,
                        current:this.state.current2,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize2,
                        total: this.state.total2,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
            );
        }else if(rowIndex==2){
            return(
                <Table
                    loading={this.state.loading3}
                    columns={columns2}
                    dataSource={this.state.data3}
                    pagination={false}
                    rowKey={record => record.id}
                    pagination={{
                        onChange: this.pageChange3,
                        current:this.state.current3,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize2,
                        total: this.state.total3,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
            );
        }else if(rowIndex==3){
            return(
                <Table
                    loading={this.state.loading4}
                    columns={columns2}
                    dataSource={this.state.data4}
                    pagination={false}
                    rowKey={record => record.id}
                    pagination={{
                        onChange: this.pageChange4,
                        current:this.state.current4,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize2,
                        total: this.state.total4,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
            );
        }else if(rowIndex==4){
            return(
                <Table
                    loading={this.state.loading5}
                    columns={columns2}
                    dataSource={this.state.data5}
                    pagination={false}
                    rowKey={record => record.id}
                    pagination={{
                        onChange: this.pageChange5,
                        current:this.state.current5,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize2,
                        total: this.state.total5,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
            );
        }else if(rowIndex==5){
            return(
                <Table
                    loading={this.state.loading6}
                    columns={columns2}
                    dataSource={this.state.data6}
                    pagination={false}
                    rowKey={record => record.id}
                    pagination={{
                        onChange: this.pageChange6,
                        current:this.state.current6,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize2,
                        total: this.state.total6,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
            );
        }

    },
    returndate2(skuCode,current2,expanded,rowIndex){
        debugger;
        if(expanded){
            this.setState1(rowIndex,skuCode,current2);
            const defaultPageSize2 = this.state.defaultPageSize2;
            const values = this.props.form.getFieldsValue();
            reqwest({
                url: '/wms/master/FindInventoryAction/findInventoryDetails',
                dataType: 'json',
                method: 'post',
                data: {skuCode:skuCode,current:current2,defaultPageSize:defaultPageSize2,
                    containerNo:values.containerNo,locationNo:values.locationNo},
                success: function (json) {
                    if(json.success){
                        console.log("数据："+json);
                        this.setState2(json,rowIndex);
                    }else{
                        message.error("加载数据失败！");
                    }
                }.bind(this),
                error: function (err) {
                    reqwestError(err);
                    message.error("加载数据失败！");
                }
            });
        }
    },
    setState1(rowIndex,skuCode,current){
        if(rowIndex==1){
            this.setState({
                loading2: true,
                skuCodeTable2:skuCode,
                current2:current,
            });
        }else if(rowIndex==2){
            this.setState({
                loading3: true,
                skuCodeTable3:skuCode,
                current3:current,
            });
        }else if(rowIndex==3){
            this.setState({
                loading4: true,
                skuCodeTable4:skuCode,
                current4:current,
            });
        }else if(rowIndex==4){
            this.setState({
                loading5: true,
                skuCodeTable5:skuCode,
                current5:current,
            });
        }else if(rowIndex==5){
            this.setState({
                loading6: true,
                skuCodeTable6:skuCode,
                current6:current,
            });
        }
    },
    setState2(json,rowIndex){
        if(rowIndex==1){
            this.setState({
                data2 : json.res,
                total2:json.count,
                loading2:false,
            });
        }else if(rowIndex==2){
            this.setState({
                data3 : json.res,
                total3:json.count,
                loading3:false,
            });
        }else if(rowIndex==3){
            this.setState({
                data4 : json.res,
                total4:json.count,
                loading4:false,
            });
        }else if(rowIndex==4){
            this.setState({
                data5 : json.res,
                total5:json.count,
                loading5:false,
            });
        }else if(rowIndex==5){
            this.setState({
                data6 : json.res,
                total6:json.count,
                loading6:false,
            });
        }
    },
    getCommodityCode(){
        reqwest({
            url: '/wms/master/FindInventoryAction/getCommodityCode',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success) {
                    console.log(json);
                    console.log(json.res);
                    this.setState({
                        commodityCodeList: json.res,
                        commodityCodeFirst: json.res[0],
                    })
                }else{
                    message.error("初始化商品代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化商品代码失败！");
            }.bind(this)
        })
    },
    getshipperId(){
        reqwest({
            url: '/wms/master/OrderInquiry/getshipperId',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success){
                    console.log(json);
                    console.log("货主代码："+json.res);
                    this.setState({
                        shipperIdList:json.res,
                        shipperIdFirst:json.res[0],
                    })
                }else{
                    message.error("初始化货主代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化货主代码失败！");
            }.bind(this)
        })
    },
    onChange(selectedRowKeys, selectedRows) {
        this.setState({selectedData: selectedRows, selectedRowKeys: selectedRowKeys});
    },
    handleReset(e) {
        e.preventDefault();
        this.props.form.resetFields();
        this.setState({selectedRowKeys: [], selectedData: []});
        this.getData(1);
    },
    reset(e){
        e.preventDefault();
        this.setState({selectedRowKeys: [], selectedData: []});
    },
    getData(current){
        this.setState({loading: true});
        let defaultPageSize = this.state.defaultPageSize;
        const values = this.props.form.getFieldsValue();
        console.log(values);
        values.currentPage = current;
        reqwest({
            url: '/wms/master/FindInventoryAction/findInventory',
            dataType: 'json',
            method: 'post',
            data: {current:values.currentPage,defaultPageSize:defaultPageSize,
                containerNo:values.containerNo,locationNo:values.locationNo,
                productId:values.productId},
            success: function (json) {
                if(json.success){
                    console.log("数据："+json.res);
                    for(var i =0;i<json.res.length;i++){
                        json.res[i].rowIndex = i+1;
                        this.returndate2(json.res[i].skuCode,1,true,i+1);
                    }
                    this.setState({data: json.res, total: json.count, loading: false});
                }else{
                    message.error("加载数据失败！");
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
                message.error("加载数据失败！");
            }.bind(this)
        });
    },
    handleSubmit(e) {
        e.preventDefault();
        this.getData(1);
    },
    pageChange(noop){
        this.setState({
            current:noop,
        })
        this.getData(noop);
    },
    pageChange2(noop){
        this.setState({
            current2:noop,
        })
        this.returndate2(this.state.skuCodeTable2,noop,true,1);
    },
    pageChange3(noop){
        this.setState({
            current3:noop,
        });
        this.returndate2(this.state.skuCodeTable3,noop,true,2);
    },
    pageChange4(noop){
        this.setState({
            current4:noop,
        });
        this.returndate2(this.state.skuCodeTable4,noop,true,3);
    },
    pageChange5(noop){
        this.setState({
            current5:noop,
        });
        this.returndate2(this.state.skuCodeTable5,noop,true,4);
    },
    pageChange6(noop){
        this.setState({
            current6:noop,
        });
        this.returndate2(this.state.skuCodeTable6,noop,true,5);
    },
    disabledDate(current){
        return current.getTime() > Date.now();
    },
    render() {
        const columns = [{
            title: '货品代码',
            dataIndex: 'skuCode',
        }, {
            title: '货品名称',
            dataIndex: 'skuName',
        }, {
            title: '总库存',
            dataIndex: 'sumQty',
        }, {
            title: '最近入库时间',
            dataIndex: 'dateTime',
        }];

        const {getFieldProps} = this.props.form;
        // const productIdProps = getFieldProps('productId',{
        //     initialValue: this.state.productId.id
        // });
        const commodityCodeProps = getFieldProps('productId', {
            initialValue:"",
        });
        const containerNoProps = getFieldProps('containerNo',{ initialValue: '' });
        const locationNoProps = getFieldProps('locationNo',{ initialValue: '' });
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const commodityCodeListSelect =[];
        commodityCodeListSelect.push(<Option value="">---请选择---</Option>);
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.skuCode}>{commodityCode.skuName}</Option>);
        });
        return (
            <div>
                <Form horizontal>
                    <Row>
                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="LocationNo："
                            >
                                <Input {...locationNoProps} />
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="托盘号："
                            >
                                <Input {...containerNoProps} />
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="商品名称："
                            >
                                <Select id="select" size="large" style={{ width: 200 }}
                                        {...commodityCodeProps} >
                                    {commodityCodeListSelect}
                                </Select>
                            </FormItem>

                            <FormItem wrapperCol={{offset: 10}}>
                                <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.handleReset}>重置</Button>
                            </FormItem>
                        </Col>
                    </Row>

                </Form>
                <Table
                    loading={this.state.loading}
                    columns={columns}
                    expandedRowRender={
                        record => this.createTable2(record.rowIndex)
                    }
                    onExpand={
                        (expanded, record) => this.returndate2(record.skuCode,1,expanded,record.rowIndex)

                    }
                    // onRowClick={
                    //     (record,index,event)=>{
                    //         console.
                    //     }
                    // }

                    rowKey={record => record.id}
                    dataSource={this.state.data}
                    pagination={{
                        onChange: this.pageChange,
                        current:this.state.current,
                        showQuickJumper: true,
                        defaultCurrent: 1,
                        defaultPageSize:this.state.defaultPageSize,
                        total: this.state.total,
                        showTotal: total => `共 ${total} 条数据`
                    }}
                />
            </div>
        );
    },
});
OutputArea = Form.create({})(OutputArea);
export default OutputArea;
