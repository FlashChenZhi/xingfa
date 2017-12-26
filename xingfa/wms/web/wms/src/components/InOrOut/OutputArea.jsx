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
        };
    },

    componentDidMount(){
        // this.getData(1);
    },

    onChange(selectedRowKeys, selectedRows) {
        this.setState({selectedData: selectedRows, selectedRowKeys: selectedRowKeys});
    },

    handleReset(e) {
        e.preventDefault();
        this.props.form.resetFields();
        this.setState({selectedRowKeys: [], selectedData: []});
    },

    cancelForce(orderNo) {
        reqwest({
            url: '/wms/opple/order/cancelOrder.do',
            method: 'POST',
            data: {orderNo: orderNo},
            type: 'json',
            error: err => {
                message.error('网络异常,请稍后再试');
            },
            success: resp => {
                if (resp.success) {
                    message.success(resp.msg);
                } else {
                    message.error(resp.msg);
                }
            }
        });
    },


    submit(e){
        e.preventDefault();
        reqwest({
            url: '/wms/inventory/outAreaSet.do',
            dataType: 'json',
            method: 'post',
            data: {outAreaData: JSON.stringify(this.state.selectedData)},
            success: function (json) {
                if (!json.success) {
                    message.error(json.msg, 10);
                } else {
                    message.success(json.msg, 10);
                    this.getData(1);
                }
            }.bind(this),
            error: function (err) {
                reqwestError(err);
            }.bind(this)
        })
    },
    reset(e){
        e.preventDefault();
        this.setState({selectedRowKeys: [], selectedData: []});
    },
    getData(current){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        if (values.lotNum) {
            values.beginDate = dateFormat(values.lotNum[0]);
            values.endDate = dateFormat(values.lotNum[1]);
        } else {
            values.beginDate = null;
            values.endDate = null;
        }
        values.currentPage = current;
        delete values.lotNum;
        reqwest({
            url: '/wms/inventory/getOutAreaData.do',
            dataType: 'json',
            method: 'post',
            data: values,
            success: function (json) {
                this.setState({data: json.msg.data, total: json.msg.total, loading: false});
            }.bind(this),
            error: function (err) {
                reqwestError(err);
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
            title: '仓库编码',
            dataIndex: 'whCode',
        }, {
            title: '订单号',
            dataIndex: 'orderNo',
        }, {
            title: '订单类型',
            dataIndex: 'jobType',
        }, {
            title: '箱数',
            dataIndex: 'boxQty',
        }, {
            title: '客户名称',
            dataIndex: 'coustomName',
        }, {
            title: '承运商',
            dataIndex: 'carrierName',
        }, {
            title: '目标货位',
            dataIndex: 'toLocation',
        }, {
            title: '区域',
            dataIndex: 'area',
        }, {
            title: '描述',
            dataIndex: 'desc',
        }, {
            title: '状态',
            dataIndex: 'status',
        },{
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 163,
                render: (text, record) => (
                <span>
                    <a onClick={this.cancelForce.bind(this, record.orderNo)}>强制取消</a >
       </span>
            )
        }];
        const {getFieldProps} = this.props.form;
        const orderNoProps = getFieldProps('orderNo');
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        return (
            <div>
                <Form horizontal>
                    <Row>
                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="订单号："
                            >
                                <Input {...orderNoProps}/>
                            </FormItem>
                        </Col>
                    </Row>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>重置</Button>
                    </FormItem>
                    <FormItem>
                        <Button type="primary" onClick={this.submit}
                                disabled={this.state.selectedData.length > 0 ? false : true}>出库</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.reset}>清除</Button>
                    </FormItem>
                </Form>
                <Table rowSelection={{onChange: this.onChange, selectedRowKeys: this.state.selectedRowKeys}}
                       loading={this.state.loading}
                       columns={columns}
                       scroll={{x: 1500}}
                       rowKey={record => record.id}
                       dataSource={this.state.data}
                       pagination={{
                           onChange: this.pageChange,
                           showQuickJumper: true,
                           defaultCurrent: 1,
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
