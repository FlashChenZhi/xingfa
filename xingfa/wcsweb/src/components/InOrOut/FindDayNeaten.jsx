import {Button,Modal,Icon , Form, Input,Radio, Table, Badge, DatePicker, Select, message, Row, Col} from 'antd';
import React from 'react';
const FormItem = Form.Item;
import reqwest from 'reqwest';
import {reqwestError, dateFormat} from '../common/Golbal';
const Option = Select.Option;
const confirm = Modal.confirm;
const MonthPicker = DatePicker.MonthPicker;
const RadioGroup = Radio.Group;
var columns2 ="";
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
            current:1,

        };
    },
    componentDidMount(){
        this.getData(1);
        this.getCommodityCode();

    },
    getCommodityCode(){
        reqwest({
            url: '/wms/master/FindDayNeatenAction/getSkuCode',
            dataType: 'json',
            method: 'post',
            data: {},
            success: function (json) {
                if(json.success) {
                    this.setState({
                        commodityCodeList: json.res,
                        commodityCodeFirst: json.res[0],
                    })
                }else{
                    message.error("初始化Sku代码失败！");
                }
            }.bind(this),
            error: function (err) {
                message.error("初始化Sku代码失败！");
            }.bind(this)
        })
    },

    onChange(selectedRowKeys, selectedRows) {
        this.setState({selectedData: selectedRows, selectedRowKeys: selectedRowKeys});
    },
    handleReset(e) {
        this.props.form.resetFields();
        this.setState({selectedRowKeys: [], selectedData: []});
        this.getData(1);
    },
    reset(e){
        e.preventDefault();
        this.setState({selectedRowKeys: [], selectedData: []});
    },
    getData(current){
        this.setState({loading: true});
        let defaultPageSize = this.state.defaultPageSize;
        const values = this.props.form.getFieldsValue();
        values.currentPage = current;
        if (values.dayDate) {
            values.dayDate = values.dayDate.format('yyyy-MM-dd');
        } else {
            values.dayDate = null;
        }
        if (values.monthDate) {
            values.monthDate = values.monthDate.format('yyyy-MM');
        } else {
            values.monthDate = null;
        }
        reqwest({
            url: '/wms/master/FindDayNeatenAction/findOutOrInWarehouse',
            dataType: 'json',
            method: 'post',
            data: {current:values.currentPage,defaultPageSize:defaultPageSize,
                productId:values.productId,dayDate:values.dayDate,
                monthDate:values.monthDate,type:values.type},
            success: function (json) {
                if(json.success){
                    console.log(json.res);
                    for(var i =0;i<json.res.length;i++){
                        json.res[i].rowIndex = i+1;
                        json.res[i].dateTime=new Date(json.res[i].dateTime).format("yyyy-MM-dd HH:mm:ss");
                    }
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
        this.getData(this.state.current);
    },
    handleSubmitstatement(e) {
        const values = this.props.form.getFieldsValue();
        if (values.dayDate) {
            values.dayDate = values.dayDate.format('yyyy-MM-dd');
        } else {
            values.dayDate = null;
        }
        if (values.monthDate) {
            values.monthDate = values.monthDate.format('yyyy-MM');
        } else {
            values.monthDate = null;
        }
        let date =null;
        if(values.type=='0'){
            if(values.dayDate == null){
                message.error("请输入日结时间！");
            }else{
                date=values.dayDate;
            }
        }else if(values.type=='1'){
            if(values.monthDate == null){
                message.error("请输入月结时间！");
            }else{
                date=values.monthDate;
            }
        }
        if(date !=null){
            window.open('/wms/master/FindDayNeatenAction/exportReport?date='+date+'&type='+values.type);
        }

    },
    pageChange(noop){
        this.setState({
            current:noop,
        })
        this.getData(noop);
    },
    disabledDate(current){
        return current.getTime() > Date.now();
    },
    render() {
        const columns = [{
            title: '货品代码',
            dataIndex: 'skuCode',
        }, {
            title: '货品名称',
            dataIndex: 'skuName',
        }, {
            title: '批次',
            dataIndex: 'lotNum',
        },{
            title: '时间',
            dataIndex: 'date',
        }, {
            title: '期初',
            dataIndex: 'benginningInventory',
        }, {
            title: '入库',
            dataIndex: 'inStorage',
        }, {
            title: '出库',
            dataIndex: 'outStorage',
        }, {
            title: '结余',
            dataIndex: 'carryover',
        }];

        const {getFieldProps} = this.props.form;
        const commodityCodeProps = getFieldProps('productId', {
            initialValue:"",
        });
        const containerNoProps = getFieldProps('containerNo',{ initialValue: '' });
        const locationNoProps = getFieldProps('locationNo',{ initialValue: '' });
        const lotNoProps = getFieldProps('lotNo',{ initialValue: '' });
        const formItemLayout = {
            labelCol: {span: 5},
            wrapperCol: {span: 14},
        };
        const createDateProps = getFieldProps('dayDate');
        const createDateProps2 = getFieldProps('monthDate');
        const radioGroupProps= getFieldProps('type',{initialValue:'0'});
        const commodityCodeListSelect =[];
        commodityCodeListSelect.push(<Option value="">---请选择---</Option>);
        this.state.commodityCodeList.forEach((commodityCode)=>{
            commodityCodeListSelect.push(<Option value={commodityCode.skuCode}>{commodityCode.skuName}</Option>);
        });
        return (
            <div>
                <Form horizontal>
                    <Row>
                        <Col lg={12}>
                            <FormItem
                                {...formItemLayout}
                                label="商品名称："
                            >
                                <Select
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    id="select" size="large" style={{ width: 200 }}
                                        {...commodityCodeProps} >
                                    {commodityCodeListSelect}
                                </Select>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="月结时间"
                            >
                                <MonthPicker  {...createDateProps2} style={{ width: 200 }}/>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="日结时间"
                            >
                                <DatePicker  {...createDateProps} style={{ width: 200 }}/>
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="查询类型"
                            >
                                <RadioGroup {...radioGroupProps}>
                                    <Radio value="0">日结</Radio>
                                    <Radio value="1">月结</Radio>
                                </RadioGroup>
                            </FormItem>

                            <FormItem wrapperCol={{offset: 10}}>
                                <Button type="primary" onClick={this.handleSubmit}>查询</Button>
                                &nbsp;&nbsp;&nbsp;
                                <Button type="ghost" onClick={this.handleReset}>重置</Button>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a onClick={this.handleSubmitstatement}>  </a>
                                <Button type="ghost"  onClick={this.handleSubmitstatement}><Icon type="download" />导出报表</Button>
                            </FormItem>
                        </Col>
                    </Row>

                </Form>
                <Table
                    loading={this.state.loading}
                    columns={columns}
                    rowKey={record => record.id}
                    dataSource={this.state.data}
                    pagination={{
                        onChange: this.pageChange,
                        current:this.state.current,
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
