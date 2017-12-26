import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const FormItem = Form.Item;
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;

let OutputPerformanceQuery = React.createClass({
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
        if (values.retrievalDate) {
            values.storeBeginDate = dateFormat(values.retrievalDate[0]);
            values.storeEndDate = dateFormat(values.retrievalDate[1]);
        } else {
            values.storeBeginDate = null;
            values.storeEndDate = null;
        }
        values.currentPage = currentPage;
        delete values.lotNum;
        delete values.retrievalDate;
        reqwest({
            url: '/wms/query/outputPerformance.do',
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
        const orderNoProps = getFieldProps('orderNo');
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
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
        }];
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
                    <FormItem>
                    </FormItem>
                    <FormItem wrapperCol={{offset: 10}}>
                        <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={this.handleReset}>重置</Button>
                    </FormItem>
                </Form>
                <Table loading={this.state.loading}
                       columns={columns}
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
OutputPerformanceQuery = Form.create({})(OutputPerformanceQuery);
export default OutputPerformanceQuery;
