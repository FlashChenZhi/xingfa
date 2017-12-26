import React from 'react';
import {Link} from 'react-router';
import {Menu, Icon, message} from 'antd';
const SubMenu = Menu.SubMenu;

const SlideMenu = React.createClass({
    getInitialState() {
        return {
            current: '1',
            openKeys: [],
        };
    },
    handleClick(e) {
        console.log('click ', e);
        this.setState({
            current: e.key,
            openKeys: e.keyPath.slice(1),
        });
    },
    onToggle(info) {
        this.setState({
            openKeys: info.open ? info.keyPath : info.keyPath.slice(1),
        });
    },
    render() {
        return (
            <aside className="ant-layout-sider">
                <div className="ant-layout-logo">
                    <div className="ant-layout-logo-child"><h2> WMS </h2></div>
                </div>
                <Menu onClick={this.handleClick}
                      openKeys={this.state.openKeys}
                      onOpen={this.onToggle}
                      onClose={this.onToggle}
                      selectedKeys={[this.state.current]}
                      mode="inline"
                      theme="dark"
                >
                    <SubMenu key="1" title={<span><Icon type="setting"/><span>入库/出库</span></span>}>
                        <Menu.Item key='1'>
                            <Link to='NewInputArea'>
                                <Icon type="setting"/><span className="nav-text">新规入库</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='2'>
                            <Link to='InputAreaAgain'>
                                <Icon type="setting"/><span className="nav-text">再入库</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='3'>
                            <Link to='OutputArea'>
                                <Icon type="setting"/><span className="nav-text">出库单出库</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='4'>
                            <Link to='InventoryOutQuery'>
                                <Icon type="setting"/><span className="nav-text">指定库存出库</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='5'>
                            <Link to='CreateReceivingPlan'>
                                <Icon type="setting"/><span className="nav-text">新建入库单</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='6'>
                            <Link to='OutSeaRetrievalSetting'>
                                <Icon type="setting"/><span className="nav-text">外贸出库设定</span>
                            </Link>
                        </Menu.Item>


                    </SubMenu>
                    <SubMenu key="2" title={<span><Icon type="setting"/><span>查询</span></span>}>
                        <Menu.Item key='4'>
                            <Link to='OnlineTaskQuery'>
                                <Icon type="setting"/><span className="nav-text">在线任务查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='5'>
                            <Link to='InputPerformanceQuery'>
                                <Icon type="setting"/><span className="nav-text">入库实绩查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='6'>
                            <Link to='OutputPerformanceQuery'>
                                <Icon type="setting"/><span className="nav-text">出库实绩查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='7'>
                            <Link to='InventoryQuery'>
                                <Icon type="setting"/><span className="nav-text">库存查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='8'>
                            <Link to='ReserveQuery'>
                                <Icon type="setting"/><span className="nav-text">出库预定查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='9'>
                            <Link to='LocationQuery'>
                                <Icon type="setting"/><span className="nav-text">货位查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='10'>
                            <Link to='BlockQuery'>
                                <Icon type="setting"/><span className="nav-text">Block查询</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='11'>
                            <Link to='MessageQuery'>
                                <Icon type="setting"/><span className="nav-text">03消息查询</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='12'>
                            <Link to='AsrsJobQuery'>
                                <Icon type="setting"/><span className="nav-text">AsrsJob查询</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='13'>
                            <Link to='RecvPlanQuery'>
                                <Icon type="setting"/><span className="nav-text">收货单查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='14'>
                            <Link to='SkuQuery'>
                                <Icon type="setting"/><span className="nav-text">商品查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='15'>
                            <Link to='ModifyLocation'>
                                <Icon type="setting"/><span className="nav-text">货位属性设定</span>
                            </Link>
                        </Menu.Item>
                    </SubMenu>
                </Menu>
            </aside>
        );
    },
});

export default SlideMenu;