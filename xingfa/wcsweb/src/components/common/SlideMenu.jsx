import React from 'react';
import {Link} from 'react-router';
import {Menu, Icon,message} from 'antd';
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
                    <SubMenu key="1" title={<span><Icon type="setting"/><span>查询</span></span>}>

                        <Menu.Item key='1'>
                            <Link to='AsrsJobQuery'>
                                <Icon type="setting"/><span className="nav-text">AsrsJob查询</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='2'>
                            <Link to='BlockQuery'>
                                <Icon type="setting"/><span className="nav-text">Block查询</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='3'>
                            <Link to='MessageQuery'>
                                <Icon type="setting"/><span className="nav-text">03&35消息查询</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='4'>
                            <Link to='MessageLogQuery'>
                                <Icon type="setting"/><span className="nav-text">消息日志查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='5'>
                            <Link to='SendMessage'>
                                <Icon type="setting"/><span className="nav-text">消息发送</span>
                            </Link>
                        </Menu.Item>

                    </SubMenu>
                    <SubMenu key="6" title={<span><Icon type="shopping-cart"/><span>入库</span></span>}>
                        <Menu.Item key='61'>
                            <Link to='PutInStorage'>
                                <Icon type="shopping-cart"/><span className="nav-text">入库</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key='64'>
                            <Link to='PlatformSwitch'>
                                <Icon type="ellipsis" /><span className="nav-text">站台模式切换</span>
                            </Link>
                        </Menu.Item>

                    </SubMenu>
                    <SubMenu key="7" title={<span><Icon type="shopping-cart"/><span>出库</span></span>}>
                        <Menu.Item key='71'>
                            <Link to='StockOutODO'>
                                <Icon type="solution" /><span className="nav-text">订单出库</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='72'>
                            <Link to='OrderInquiry'>
                                <Icon type="book"/><span className="nav-text">订单查询</span>
                            </Link>
                        </Menu.Item>
                        {/*<Menu.Item key='72'>
                            <Link to='FindOrderDetail'>
                                <Icon type="solution" /><span className="nav-text">订单详情</span>
                            </Link>
                        </Menu.Item>*/}
                        <Menu.Item key='73'>
                            <Link to='AssignsTheStorehouse'>
                                <Icon type="solution" /><span className="nav-text">定点出库ML01</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='74'>
                            <Link to='AssignsTheStorehouseML02'>
                                <Icon type="solution" /><span className="nav-text">定点出库ML02</span>
                            </Link>
                        </Menu.Item>
                    </SubMenu>
                    <SubMenu key="8" title={<span><Icon type="shopping-cart"/><span>库存整理</span></span>}>

                        <Menu.Item key='81'>
                            <Link to='CheckInStorage'>
                                <Icon type="shopping-cart"/><span className="nav-text">抽检入库</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='82'>
                            <Link to='SampleSurvey'>
                                <Icon type="solution" /><span className="nav-text">抽检出库</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='83'>
                            <Link to='MoveStorage'>
                                <Icon type="solution" /><span className="nav-text">移库</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='84'>
                            <Link to='TrimStorage'>
                                <Icon type="solution" /><span className="nav-text">理货</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='85'>
                            <Link to='FindInventory'>
                                <Icon type="solution" /><span className="nav-text">库存展示</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='86'>
                            <Link to='FindOutOrInWarehouse'>
                                <Icon type="solution" /><span className="nav-text">出入库查询</span>
                            </Link>
                        </Menu.Item>
                        <Menu.Item key='87'>
                            <Link to='FindDayNeaten'>
                                <Icon type="solution" /><span className="nav-text">日结查询</span>
                            </Link>
                        </Menu.Item>
                    </SubMenu>

                </Menu>
            </aside>
        );
    },
});

export default SlideMenu;