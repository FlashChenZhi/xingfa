import {Button, Form, Table, Pagination, Select, message, Row, Col, Input, DatePicker} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
import ChangeLevelModel from './ChangeLevelModel';
import AddScarModel from './AddScarModel';

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
    } else if (s === '3') {
        return "充电中";
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
            blockNo: '',
            changeLevelModel: false,
            addScarModel: false

        }
    },
    componentDidMount(){
        this.getTableData(1);
    },
    getTableData(currentPage){
        this.setState({loading: true});
        const values = this.props.form.getFieldsValue();
        values.currentPage = currentPage;
        reqwest({
            url: '/wcs/webService/searchBlock.do',
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
            url: '/wcs/webService/onLine.do',
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
                this.getTableData(1);

            }
        });
    },

    offLine(blockNo) {
        reqwest({
            url: '/wcs/webService/offLine.do',
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
                this.getTableData(1);

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

    cancelWaiting(blockNo) {
        reqwest({
            url: '/wcs/webService/cancelWaiting.do',
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

    moveScar(blockNo){
        reqwest({
            url: '/wcs/webService/moveScar.do',
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

    recoveryException(blockNo){
        reqwest({
            url: '/wcs/webService/recovryException.do',
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
    deleteData(blockNo){
        reqwest({
            url: '/wcs/webService/deleteData.do',
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
    onTheMLCar(blockNo){
        reqwest({
            url: '/wcs/webService/onTheMLCar.do',
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
    getTheSCCar(blockNo){
        reqwest({
            url: '/wcs/webService/getTheSCCar.do',
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

    chargeStart(blockNo){
        reqwest({
            url: '/wcs/webService/chargeStart.do',
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

    chargeFinish(blockNo){
        reqwest({
            url: '/wcs/webService/chargeFinish.do',
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

    constraintChargeFinish(blockNo){
        reqwest({
            url: '/wcs/webService/constraintChargeFinish.do',
            method: 'POST',
            data: {blockNo: blockNo},
            type: 'json',
            error: err => {
                message.error('网络异常,请稍后再试');
            },
            success: resp => {
                if (resp.success) {
                    message.success(resp.msg);
                    this.getTableData(1);
                } else {
                    message.error(resp.msg);
                }
            }
        });
    },

    addScar(blockNo){
        this.setState({blockNo: blockNo, addScarModel: true});
    },

    changeLevel(blockNo) {
        this.setState({blockNo: blockNo, changeLevelModel: true});
    },

    hideChangeLevelModel() {
        this.setState({blockNo: null, changeLevelModel: false});
    },

    hideaddScarModel() {
        this.setState({blockNo: null, addScarModel: false});
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
            title: '所在行',
            dataIndex: 'bank',
        }, {
            title: '所在列',
            dataIndex: 'bay',
        },{
            title: '所在层',
            dataIndex: 'level',
        },{
            title: '电量',
            dataIndex: 'power',
        },{
            title: '是否等待回复',
            dataIndex: 'waitResponse',
            key: 'waitResponse',
            render: (text, record) => {
                return boolCmp(record.waitResponse);
            }
        },{
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 250,
            render: (text, record) =>{
                const blockNoStr = record.blockNo.substr(0,2);
                const blockNo = record.blockNo;
                return(
                    <span>
                        <a onClick={this.onLine.bind(this, record.blockNo)}>运行</a>
                        &nbsp;&nbsp;||&nbsp;&nbsp;
                        <a onClick={this.offLine.bind(this, record.blockNo)}>切离</a >
                        &nbsp;&nbsp;||&nbsp;&nbsp;
                        <a onClick={this.cancelWaiting.bind(this, record.blockNo)}>取消等待</a >
                        &nbsp;&nbsp;||&nbsp;&nbsp;
                        <a onClick={this.chargeStart.bind(this, record.blockNo)}>充电开始</a >
                        &nbsp;&nbsp;||&nbsp;&nbsp;
                        <a onClick={this.chargeFinish.bind(this, record.blockNo)}>充电完成</a >
                        &nbsp;&nbsp;||&nbsp;&nbsp;
                        <a onClick={this.recoveryException.bind(this, record.blockNo)}>解除异常</a >
                        &nbsp;&nbsp;||&nbsp;&nbsp;
                        <a onClick={this.deleteData.bind(this, record.blockNo)}>清除数据</a >
                        {
                            blockNoStr=='SC'?
                            <span>&nbsp;&nbsp;||&nbsp;&nbsp;
                            <a onClick={this.onTheMLCar.bind(this, record.blockNo)}>上车</a >
                            </span>
                            :
                            blockNoStr=='ML'?
                            <span>&nbsp;&nbsp;||&nbsp;&nbsp;
                                    <a onClick={this.getTheSCCar.bind(this, record.blockNo)}>接车</a >
                            </span>
                                :
                                <span></span>

                        }
                        {
                            blockNo=='SC01'?
                                <span>&nbsp;&nbsp;||&nbsp;&nbsp;
                                    <a onClick={this.constraintChargeFinish.bind(this, record.blockNo)}>强制充电完成</a >
                            </span>
                                :
                                <span></span>
                        }
                    </span>
                );
            }
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
                <ChangeLevelModel
                    code={this.state.blockNo}
                    visible={this.state.changeLevelModel}
                    hideModel={this.hideChangeLevelModel.bind(this)}
                />
                <AddScarModel
                    code={this.state.blockNo}
                    visible={this.state.addScarModel}
                    hideModel={this.hideaddScarModel.bind(this)}
                />

            </div>
        );
    },
});
BlockQuery = Form.create({})(BlockQuery);
export default BlockQuery;
