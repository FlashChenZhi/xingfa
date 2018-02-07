import {Button, Form, Input, Table, Pagination, DatePicker, InputNumber, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;
let OutputArea = React.createClass({
    getInitialState(){
        return {
            data: [],//表格数据
            total: 0,//表格数据总行数
            selectedData: [],//点击设定提交到后台的数据
            loading: false,
            selectedRowKeys: [],
            defaultPageSize:10,
            commodityCodeList:[],
            commodityCodeFirst:"",
            shipperIdList:[],
            shipperIdFirst:"",

        };
    },
    componentDidMount(){
        this.getData(1);
        this.getCommodityCode();
        this.getshipperId();
    },
    getCommodityCode(){
        reqwest({
            url: '/wms/master/OrderInquiry/getCommodityCode',
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
            url: '/wms/master/OrderInquiry/findOrder',
            dataType: 'json',
            method: 'post',
            data: {orderNo:values.orderNo,currentPage:values.currentPage,productId:values.productId
                ,shipperId:values.shipperId, PageSize:defaultPageSize},
            success: function (json) {
                if(json.success){
                    console.log("数据："+json.res);
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
        this.getData(noop);
    },
    disabledDate(current){
        return current.getTime() > Date.now();
    },
    render() {
        const columns = [{
            title: '订单号',
            dataIndex: 'orderNo',
        }, {
            title: '商品代码',
            dataIndex: 'productId',
        }, {
            title: '商品名称',
            dataIndex: 'productName',
        }, {
            title: '货主代码',
            dataIndex: 'shipperId',
        }, {
            title: '货主名称',
            dataIndex: 'shipperName',
        }, {
            title: '数量',
            dataIndex: 'productNum',
        }, {
            title: '已分配数量',
            dataIndex: 'allcatedNum',
        }];
        const {getFieldProps} = this.props.form;
        // const productIdProps = getFieldProps('productId',{
        //     initialValue: this.state.productId.id
        // });
        const commodityCodeProps = getFieldProps('productId', {
            initialValue:"",
        });
        const shipperIdProps = getFieldProps('shipperId',{
            initialValue: "",
        });
        const orderNoProps = getFieldProps('orderNo',{ initialValue: '' });
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const commodityCodeListSelect =[];
        commodityCodeListSelect.push(<Option value="">---请选择---</Option>);
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.id}>{commodityCode.name}</Option>);
        });
        const shipperIdList =[];
        shipperIdList.push(<Option value="">---请选择---</Option>);
        this.state.shipperIdList.forEach((shipperId)=>{
            shipperIdList.push(<Option value={shipperId.id}>{shipperId.name}</Option>);
        });
        return (
            <div>
                <Form horizontal>
                    <Row>
                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="订单号："
                            >
                                <Input {...orderNoProps} />
                            </FormItem>
                            {/*<FormItem*/}
                                {/*{...formItemLayout}*/}
                                {/*label="商品代码："*/}
                            {/*>*/}
                                {/*<Input {...productIdProps}/>*/}
                            {/*</FormItem>*/}
                            <FormItem
                                {...formItemLayout}
                                label="商品代码："
                            >
                                <Select id="select" size="large" style={{ width: 200 }}
                                        {...commodityCodeProps} >
                                    {commodityCodeListSelect}
                                </Select>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="货主代码："
                            >
                                <Select id="select" size="large" style={{ width: 200 }}
                                         {...shipperIdProps} >
                                    {shipperIdList}
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
                <Table rowSelection={{onChange: this.onChange, selectedRowKeys: this.state.selectedRowKeys}}
                       loading={this.state.loading}
                       columns={columns}
                       // scroll={{x: 1500}}
                       rowKey={record => record.id}
                       dataSource={this.state.data}
                       pagination={{
                           onChange: this.pageChange,
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
