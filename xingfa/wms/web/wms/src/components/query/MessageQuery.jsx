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

const typeCmp = (s) => {
    if (s === '02') {
        return "取货";
    } else if (s === '03') {
        return '卸货';
    } else if (s === '04') {
        return '移动';
    } else if (s == '05') {
        return '接车';
    } else if (s === '06') {
        return '卸车';
    } else if (s === '07') {
        return '移栽取货';
    } else if (s === '08') {
        return '移栽卸货';
    } else if (s === '09') {
        return '上车';
    } else if (s === '10') {
        return '下车';
    } else {
        return "切离";
    }
};

let MessageQuery = React.createClass({
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
            url: '/wms/message/searchMessage.do',
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

    send(id) {
        reqwest({
            url: '/wms/message/sendMsg.do',
            method: 'POST',
            data: {id: id},
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
            title: 'id',
            dataIndex: 'id',
            key: 'id',
        }, {
            title: '类型',
            dataIndex: 'cycleOrder',
            key: 'cycleOrder',
            render: (text, record)=> {
                return typeCmp(record.cycleOrder);
            }
        }, {
            title: 'mcKey',
            dataIndex: 'mcKey',
        }, {
            title: 'station',
            dataIndex: 'station',
        }, {
            title: 'dock',
            dataIndex: 'dock',
        }, {
            title: 'machineNo',
            dataIndex: 'machineNo',
        }, {
            title: 'bank',
            dataIndex: 'bank',
        }, {
            title: 'bay',
            dataIndex: 'bay',
        }, {
            title: 'level',
            dataIndex: 'level',
        }, {
            title: '是否回复',
            dataIndex: 'received',
            key: 'received',
            render: (text, record)=> {
                return boolCmp(record.received);
            }
        }, {
            title: 'sendDate',
            dataIndex: 'sendDate',
        }, {
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 163,
            render: (text, record) => (
                <span>
                 <a onClick={this.send.bind(this, record.id)}>重发</a>
       </span>
            )
        }];

        const {getFieldProps} = this.props.form;
        const mcKeyProps = getFieldProps('mcKey');
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
                                label="Mckey："
                            >
                                <Input {...mcKeyProps}/>
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
MessageQuery = Form.create({})(MessageQuery);
export default MessageQuery;
