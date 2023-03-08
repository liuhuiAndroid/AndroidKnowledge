import React from 'react';
import {View} from 'react-native';

export type IAuthorityType =
  | undefined
  | string
  | string[]
  | Promise<boolean>
  | ((currentAuthority: string | string[]) => IAuthorityType);

export interface IProps {
  authority: boolean;
  noMatch?: () => JSX.Element;
}

class Authorized extends React.PureComponent<IProps> {
  render() {
    const {children, authority, noMatch = null} = this.props;
    if (authority) {
      return children;
    }
    return <View>{noMatch && noMatch()}</View>;
  }
}

export default Authorized;
