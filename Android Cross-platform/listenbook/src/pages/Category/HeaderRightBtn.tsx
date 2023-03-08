import React from 'react';
import {HeaderButtons, Item} from 'react-navigation-header-buttons';
import {RootState} from '@/models/index';
import {connect, ConnectedProps} from 'react-redux';

const mapStateToProps = ({category}: RootState) => ({
  isEdit: category.isEdit,
});

const connector = connect(mapStateToProps);

interface IProps extends ConnectedProps<typeof connector> {
  onPress: () => void;
}

class HeaderRightBtn extends React.Component<IProps> {
  onPress = () => {
    const {onPress} = this.props;
    if (typeof onPress === 'function') {
      onPress();
    }
  };
  render() {
    const {isEdit} = this.props;
    return (
      <HeaderButtons>
        <Item title={isEdit ? '完成' : '编辑'} onPress={this.onPress} />
      </HeaderButtons>
    );
  }
}

export default connector(HeaderRightBtn);
