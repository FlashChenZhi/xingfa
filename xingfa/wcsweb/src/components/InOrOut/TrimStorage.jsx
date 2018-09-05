import {Button, Form, Input, Pagination, InputNumber, Select, message, } from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

const Option = Select.Option;


let PlatformSwitch = React.createClass({
    getInitialState(){
        return {
            loading: false,
            selectedRowKeys: [],
            zhantai:"1301",
        };
    },
    componentDidMount(){
        //this.findPlatformSwitch();
    },

    submit(e){
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let bay= values.bay;
                let level= values.level;
                reqwest({
                    url: '/wms/master/trimStorageAction/addTrimStorage',
                    dataType: 'json',
                    method: 'post',
                    data: {bay: bay,level:level},
                    success: function (json) {
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success(json.msg);
                        }
                        // this.handleReset(e);
                    }.bind(this),
                    error: function (err) {
                        message.error("任务创建失败！");
                        this.handleReset(e);
                    }.bind(this)
                })
            }
        });

    },
    /**
     * 重置表单
     * @param e
     */
    handleReset(e) {
        console.log("进入清除！");
        this.props.form.resetFields();
    },

    render() {
        const {getFieldProps } = this.props.form;
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const bayProps = getFieldProps('bay');
        const levelProps = getFieldProps('level');
        return (
            <div>
                <Form horizontal >
                    <br/><br/>
                    <FormItem
                        {...formItemLayout}
                        label="层数："
                    >
                        <Input style={{width:"300"}}
                               {...levelProps}   placeholder="请输入层数" />
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="列数："
                    >
                        <Input style={{width:"300"}}
                               {...bayProps}   placeholder="请输入列数" />
                    </FormItem>
                    <br/><br/>
                    <FormItem wrapperCol={{offset: 6}}>
                        <Button type="primary" onClick={this.submit}
                        //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >设定</Button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <Button type="primary" onClick={this.handleReset}
                            //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >重置</Button>
                    </FormItem>
                </Form>
            </div>
        );
    },
});
PlatformSwitch = Form.create()(PlatformSwitch);
export default PlatformSwitch;
