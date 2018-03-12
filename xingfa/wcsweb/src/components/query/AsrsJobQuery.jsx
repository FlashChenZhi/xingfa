import {Button, Form, Input, Table, Pagination, message} from 'antd';
import React from 'react';
import reqwest from 'reqwest';
import {reqwestError} from '../common/Golbal';

let AsrsJobQuery = React.createClass({
    getInitialState() {
        return {
            data: [],
            total: 0,//表格数据总行数
            loading: false,
            current:1,
        }
    },
    componentDidMount(){
         //this.getTableData(1);
    },
    getTableData(currentPage){

        this.setState({loading: true});
        reqwest({
            url: '/wcs/webService/searchJob.do',
            dataType: 'json',
            method: 'post',
            data: {currentPage:currentPage},
            success: function (json) {
                this.setState({data: json.msg.data, total: json.msg.total, loading: false});
            }.bind(this),
            error: function (err) {
                reqwestError(err);
            }.bind(this)
        });
    },
    pageChange(noop){
        this.setState({
            current:noop,
        });
        this.getTableData(noop);
    },

    search(){
        this.pageChange(1);
    },

    deleteJob(mckey) {
        reqwest({
            // url: '/wcs/webService/deleteJob.do',
            url: '/wms/task/cancelJob',
            method: 'POST',
            data: {mckey: mckey},
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

    render() {

        const columns = [{
            title: 'MCKEY',
            dataIndex: 'mcKey',
        }, {
            title: '源货位号',
            dataIndex: 'fromLocation',
        }, {
            title: '目标货位号',
            dataIndex: 'toLocation',
        }, {
            title: '托盘号',
            dataIndex: 'barcode',
        }, {
            title: '源站台',
            dataIndex: 'fromStation',
        }, {
            title: '目标站台',
            dataIndex: 'toStation',
        }, {
            title: '作业类型',
            dataIndex: 'jobType',
        }, {
            title: '作业状态',
            dataIndex: 'jobStatus',
        },{
            title: '操作', dataIndex: 'operation', key: 'operation', fixed: 'right', width: 250,
            render: (text, record) => (

                <span>
                     <a onClick={this.deleteJob.bind(this, record.mcKey)}>删除任务</a>
                </span>
            )
        }];

        return (
            <div>
                <Table loading={this.state.loading}
                       columns={columns}
                       rowKey={record => record.mcKey}
                       dataSource={this.state.data}
                       pagination={{
                           onChange: this.pageChange,
                           showQuickJumper: true,
                           defaultCurrent: 1,
                           total: this.state.total,
                           current:this.state.current,
                           showTotal: total => `共 ${total} 条数据`
                       }}
                />
                <Button type="primary" onClick={this.search}>查询</Button>
            </div>
        );
    },
});
export default AsrsJobQuery;
