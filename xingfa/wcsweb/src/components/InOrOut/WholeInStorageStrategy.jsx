import {Button,Modal,Icon , Form, Input,Radio, Table, Badge, DatePicker, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const Option = Select.Option;
const confirm = Modal.confirm;
const RangePicker = DatePicker.RangePicker;
const RadioGroup = Radio.Group;
var columns2 ="";
let OutputArea = React.createClass({
    getInitialState(){
        return {
            lotNum: "",//表格数据
            isManual: "",//表格数据总行数
            status:"",

        };
    },
    componentDidMount(){
        this.getData();

    },
    onChange(selectedRowKeys, selectedRows) {
        this.setState({selectedData: selectedRows, selectedRowKeys: selectedRowKeys});
    },
    handleReset(e) {
        this.props.form.resetFields();
        this.getData();
    },
    reset(e){
        e.preventDefault();
        this.setState({selectedRowKeys: [], selectedData: []});
    },
    getData(){
        reqwest({
            url: '/wms/master/WholeInStorageStrategyAction/getWholeInStorageStrategy',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success){
                    console.log(json.res);
                    this.setState({
                        status: json.res.status,
                    });
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
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let status= values.status;
                reqwest({
                    url: '/wms/master/WholeInStorageStrategyAction/updateWholeInStorageStrategy',
                    dataType: 'json',
                    method: 'post',
                    data: {status: status},
                    success: function (json) {
                        console.log(json)
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success("设定成功！");
                            this.handleReset(e);
                        }
                    }.bind(this),
                    error: function (err) {
                        message.error("设定任务失败！");
                        this.handleReset(e);
                    }.bind(this)
                })
            }
        });

    },

    render() {
        const {getFieldProps} = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const radioGroupProps= getFieldProps('status',{initialValue:this.state.status});
        return (
            <div>
                <Form horizontal>
                    <Row>
                        <Col lg={12} >

                            <FormItem
                                {...formItemLayout}
                                label="全局入库策略状态："
                            >
                                <RadioGroup {...radioGroupProps}>
                                    <Radio value="0">手动</Radio>
                                    <Radio value="1">自动</Radio>
                                </RadioGroup>
                            </FormItem>
                            <FormItem wrapperCol={{offset: 10}}>
                                <Button type="primary" onClick={this.handleSubmit}>提交</Button>
                            </FormItem>
                        </Col>
                    </Row>

                </Form>
            </div>
        );
    },
});
OutputArea = Form.create({})(OutputArea);
export default OutputArea;
