import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const FormItem = Form.Item;

const boolCmp = (b) => {
    if (b === true) {
        return "是";
    } else if (b === false) {
        return "否";
    } else {
        return "null";
    }
};


const statusCmp = (s) => {
    if (s === '1') {
        return "运行";
    } else {
        return "切离";
    }
};

let BlockQuery = React.createClass({
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
        values.currentPage = currentPage;
        reqwest({
            url: '/wms/block/searchBlock.do',
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

    onLine(blockNo) {
        reqwest({
            url: '/wms/block/onLine.do',
            method: 'POST',
            data: {blockNo: blockNo},
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

    offLine(blockNo) {
        reqwest({
            url: '/wms/block/offLine.do',
            method: 'POST',
            data: {blockNo: blockNo},
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

    onCar(blockNo) {
        reqwest({
            url: '/wms/block/onCar.do',
            method: 'POST',
            data: {blockNo: blockNo},
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

    charge(blockNo) {
        reqwest({
            url: '/wms/block/charge.do',
            method: 'POST',
            data: {blockNo: blockNo},
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

    chargeover(blockNo) {
        reqwest({
            url: '/wms/block/chargeover.do',
            method: 'POST',
            data: {blockNo: blockNo},
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


    cancelWaiting(blockNo) {
        reqwest({
            url: '/wms/block/cancelWaiting.do',
            method: 'POST',
            data: {blockNo: blockNo},
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

    render() {

        const columns = [{
            title: 'BlockNo',
            dataIndex: 'blockNo',
        }, {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            render: (text, record) => {
                return statusCmp(record.status);
            }
        }, {
            title: 'McKey',
            dataIndex: 'mcKey',
        }, {
            title: 'ReservMcKey',
            dataIndex: 'reservMcKey',
        }, {
            title: 'sCarNo',
            dataIndex: 'sCarNo',
        }, {
            title: 'mCarNo',
            dataIndex: 'mCarNo',
        }, {
            title: '是否等待回复',
            dataIndex: 'waitResponse',
            key: 'waitResponse',
            render: (text, record) => {
                return boolCmp(record.waitResponse);
            }
        }, {
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 300,
            render: (text, record) => (
                <span>
                 <a onClick={this.onLine.bind(this, record.blockNo)}>运行</a>&nbsp;&nbsp;||&nbsp;&nbsp;
                    <a onClick={this.offLine.bind(this, record.blockNo)}>切离</a >
                    &nbsp;&nbsp;||&nbsp;&nbsp;
                    <a onClick={this.onCar.bind(this, record.blockNo)}>上车</a >
                    &nbsp;&nbsp;||&nbsp;&nbsp;
                    <a onClick={this.cancelWaiting.bind(this, record.blockNo)}>取消等待</a >
                    &nbsp;&nbsp;||&nbsp;&nbsp;
                    <a onClick={this.charge.bind(this, record.blockNo)}>充电</a >
                    &nbsp;&nbsp;||&nbsp;&nbsp;
                    <a onClick={this.chargeover.bind(this, record.blockNo)}>充电完成</a >
       </span>
            )
        }];

        const {getFieldProps} = this.props.form;
        const blockNoProps = getFieldProps('blockNo');
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
                                label="BlockNo："
                            >
                                <Input {...blockNoProps}/>
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
BlockQuery = Form.create({})(BlockQuery);
export default BlockQuery;
