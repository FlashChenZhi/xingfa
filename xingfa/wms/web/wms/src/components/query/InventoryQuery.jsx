import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const FormItem = Form.Item;
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;

const columns = [{
    title: '商品编码',
    dataIndex: 'skuCode',
},{
    title: '数量',
    dataIndex: 'qty',
},  {
    title: '订单号',
    dataIndex: 'orderNo',
}, {
    title: '批次',
    dataIndex: 'lotNum',
}, {
    title: '货位位置',
    dataIndex: 'locationNo',
}, {
    title: '托盘号',
    dataIndex: 'palletBarcode',
}];
let InventoryQuery = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
        }
    },
    componentDidMount(){
        // this.getTableData(1);
    },
    getTableData(currentPage){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        if (values.lotNum) {
            values.lotNumBeginDate = dateFormat(values.lotNum[0]);
            values.lotNumEndDate = dateFormat(values.lotNum[1]);
        } else {
            values.lotNumBeginDate = null;
            values.lotNumEndDate = null;
        }
        if (values.storeDate) {
            values.storeBeginDate = dateFormat(values.storeDate[0]);
            values.storeEndDate = dateFormat(values.storeDate[1]);
        } else {
            values.storeBeginDate = null;
            values.storeEndDate = null;
        }
        values.currentPage = currentPage;
        delete values.lotNum;
        delete values.storeDate;
        reqwest({
            url: '/wms/query/inventory.do',
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
    pageChange(noop){
        this.getTableData(noop);
    },
    handleSubmit(e) {
        e.preventDefault();
        this.getTableData(1);
    },
    handleReset(e) {
        e.preventDefault();
        this.props.form.resetFields();
    },

    disabledDate(current){
        return current.getTime() > Date.now();
    },
    render() {
        const {getFieldProps} = this.props.form;
        const skuCodeProps = getFieldProps('skuCode');
        const lotNumProps = getFieldProps('lotNum');
        const locationNoProps = getFieldProps('locationNo');
        const barcodeProps = getFieldProps('palletBarcode');
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
                                label="商品编号："
                            >
                                <Input {...skuCodeProps}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="货位位置："
                            >
                                <Input {...locationNoProps}/>
                            </FormItem>
                        </Col>
                        <Col lg={12}>

                            <FormItem
                                {...formItemLayout}
                                label="批次："
                            >
                                <RangePicker {...lotNumProps} format="yyyyMMdd"
                                             disabledDate={this.disabledDate}/>
                            </FormItem>

                            <FormItem
                                {...formItemLayout}
                                label="托盘号："
                            >
                                <Input {...barcodeProps}/>
                            </FormItem>

                        </Col>
                    </Row>
                    <FormItem>
                    </FormItem>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>重置</Button>
                        &nbsp;&nbsp;&nbsp;
                        <a href="http://localhost:8080/wms/LocationList.html" target="_blank">货位Laylow图</a>

                    </FormItem>
                </Form>
                <Table loading={this.state.loading}
                       columns={columns}
                       rowKey={record => record.id}
                       dataSource={this.state.data}
                       scroll={{x: 1500}}
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
InventoryQuery = Form.create({})(InventoryQuery);
export default InventoryQuery;
