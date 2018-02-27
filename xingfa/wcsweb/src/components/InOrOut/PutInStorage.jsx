import {Button, Form, Input, Pagination, InputNumber, Select, message, } from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';

const Option = Select.Option;


let PutInStorage = React.createClass({
    getInitialState(){
        return {
            tuopanhao: "",//托盘号
            loading: false,
            selectedRowKeys: [],
            commodityCodeList:[],//货品代码集合
            commodityCodeFirst:"",//货品代码第一个
        };
    },
    componentDidMount(){
        this.getCommodityCode();
    },
    getCommodityCode(){
        reqwest({
            url: '/wms/master/putInStorage/getCommodityCode',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success){
                    console.log(json);
                    console.log(json.res);
                    this.setState({
                        commodityCodeList:json.res,
                        commodityCodeFirst:json.res[0],
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
    submit(e){
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
                let tuopanhao= encodeURI(values.tuopanhao,"utf-8");
                let zhantai= values.zhantai;
                let commodityCode= values.commodityCode;
                let num= values.Num;
                reqwest({
                    url: '/wms/master/putInStorage/addTask',
                    dataType: 'json',
                    method: 'post',
                    data: {tuopanhao: tuopanhao,zhantai:zhantai,commodityCode:commodityCode,num:num},
                    success: function (json) {
                        if (!json.success) {
                            message.error(json.msg);
                        } else {
                            message.success("设定任务成功！");
                        }
                        this.handleReset(e);
                    }.bind(this),
                    error: function (err) {
                        message.error("设定任务失败！");
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

        const tuopanhaoProps = getFieldProps('tuopanhao', {
            rules: [{ required: true, message: '请扫描托盘号！' }]
        });
        const NumProps = getFieldProps('Num', {
            rules: [{ required: true, message: '请输入数量！' }],
            initialValue:"1",
        });
        const commodityCodeProps = getFieldProps('commodityCode', {
            initialValue:this.state.commodityCodeFirst.id,
        });

        const commodityCodeListSelect =[];
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.id}>{commodityCode.name}</Option>);
        });
        return (
            <div>
                <Form horizontal >
                    <br/><br/>
                    <FormItem
                        {...formItemLayout}
                        label="托盘号："
                    >

                        <Input style={{width:"300"}}
                            {...tuopanhaoProps}   placeholder="请扫描托盘号" />
                    </FormItem>
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
                        label="数量："
                    >

                        {/*<Input style={{width:"300"}}*/}
                               {/*{...NumProps}   placeholder="请输入数量" />*/}
                        <InputNumber min={1} defaultValue={2}   {...NumProps}  />
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        label="站台："
                    >
                        <Select id="select" size="large" defaultValue="1101" style={{ width: 200 }}
                        {...getFieldProps('zhantai', { initialValue: '1101' })} >
                            <Option value="1101">1101</Option>
                            <Option value="1301">1301</Option>
                        </Select>
                    </FormItem><br/><br/>
                    <FormItem wrapperCol={{offset: 6}}>
                        <Button type="primary" onClick={this.submit}
                                //disabled={this.state.tuopanhao.length > 0 ? false : true}
                        >设定</Button>
                    </FormItem>
                </Form>
            </div>
        );
    },
});
PutInStorage = Form.create()(PutInStorage);
export default PutInStorage;
