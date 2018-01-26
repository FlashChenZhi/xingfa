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
                </Menu>
            </aside>
        );
    },
});

export default SlideMenu;