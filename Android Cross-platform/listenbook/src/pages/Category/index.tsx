import React, {Component} from 'react';
import {View, Text, StyleSheet} from 'react-native';
import _ from 'lodash';
import {DragSortableView} from 'react-native-drag-sort';
import {connect, ConnectedProps} from 'react-redux';
import {ScrollView} from 'react-native-gesture-handler';
import HeaderRightBtn from './HeaderRightBtn';
import {RootState} from '../../models';
import {ICategory} from '@/models/category';
import {RootStackNavigation} from '@/navigator/index';
import Item, {parentWidth, itemWidth, itemHeight, marginTop} from './Item';
import Touchable from '@/components/Touchable';

const fixedItems = [0, 1];

const mapStateToProps = ({category}: RootState) => ({
  isEdit: category.isEdit,
  categorys: category.categorys,
  myCategorys: category.myCategorys,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  navigation: RootStackNavigation;
}

export interface IState {
  myCategorys: ICategory[];
}

export {HeaderRightBtn};

/**
 * 分类模块
 */
class Category extends Component<IProps, IState> {
  constructor(props: IProps) {
    super(props);
    this.state = {
      myCategorys: props.myCategorys,
    };
    props.navigation.setOptions({
      headerRight: () => <HeaderRightBtn onPress={this.onSubmit} />,
    });
  }

  componentWillUnmount() {
    const {dispatch, isEdit} = this.props;
    if (isEdit) {
      dispatch({
        type: 'category/setState',
        payload: {
          isEdit: false,
        },
      });
    }
  }

  onSubmit = () => {
    const {dispatch, isEdit, navigation} = this.props;
    const {myCategorys} = this.state;
    dispatch({
      type: 'category/toggle',
      payload: {
        myCategorys,
      },
    });
    if (isEdit) {
      navigation.goBack();
    }
  };

  onLongPress = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'category/setState',
      payload: {
        isEdit: true,
      },
    });
  };

  onPress = (item: ICategory, selected: boolean) => {
    const {isEdit} = this.props;
    const {myCategorys} = this.state;
    if (isEdit) {
      let newMyCategorys: ICategory[];
      if (selected) {
        newMyCategorys = myCategorys.filter(
          selectedItem => selectedItem.id !== item.id,
        );
      } else {
        newMyCategorys = myCategorys.concat(item);
      }
      this.setState({
        myCategorys: newMyCategorys,
      });
    }
  };

  onClickItem = (data: ICategory[], item: ICategory, index: number) => {
    const fixed = fixedItems.includes(index);
    if (fixed) {
      return;
    }
    this.onPress(item, true);
  };

  onDataChange = (data: ICategory[]) => {
    this.setState({
      myCategorys: data,
    });
  };

  renderItem = (item: ICategory, index: number) => {
    const {isEdit} = this.props;
    const disabled = fixedItems.includes(index);
    return (
      <Item
        key={item.id}
        fixedItems={fixedItems}
        index={index}
        isEdit={isEdit}
        data={item}
        selected
        disabled={disabled}
      />
    );
  };

  renderUnSelectedItem = (item: ICategory, index: number) => {
    const {isEdit} = this.props;
    return (
      <Touchable
        key={item.id}
        onPress={() => this.onPress(item, false)}
        onLongPress={this.onLongPress}>
        <Item
          key={item.id}
          fixedItems={fixedItems}
          index={index}
          isEdit={isEdit}
          data={item}
          selected={false}
          disabled={false}
        />
      </Touchable>
    );
  };

  render() {
    const {myCategorys} = this.state;
    const {isEdit, categorys} = this.props;
    const categoryByCassify = _.groupBy(
      categorys,
      (item: ICategory) => item.classify,
    );
    return (
      <ScrollView style={styles.contaner}>
        <View>
          <View>
            <Text style={styles.classifyName}>
              我的分类<Text style={styles.tips}>长按可拖动顺序</Text>
            </Text>
          </View>
          <View style={styles.classifyView}>
            <DragSortableView
              dataSource={myCategorys}
              parentWidth={parentWidth}
              childrenWidth={itemWidth}
              childrenHeight={itemHeight}
              marginChildrenTop={marginTop}
              fixedItems={fixedItems}
              sortable={isEdit}
              onClickItem={this.onClickItem}
              onDataChange={this.onDataChange}
              keyExtractor={item => item.id} // FlatList作用一样，优化
              renderItem={this.renderItem}
            />
          </View>
        </View>
        {Object.keys(categoryByCassify).map(key => {
          const allTypeList = categoryByCassify[key].filter(
            item => !myCategorys.some(item2 => item2.id === item.id),
          );
          if (allTypeList.length === 0) {
            return null;
          }
          return (
            <View key={key}>
              <View>
                <Text style={styles.classifyName}>{key}</Text>
              </View>
              <View style={styles.classifyView}>
                {allTypeList.map(this.renderUnSelectedItem)}
              </View>
            </View>
          );
        })}
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  contaner: {
    flex: 1,
    backgroundColor: '#f3f6f6',
  },
  classifyName: {
    fontSize: 16,
    marginBottom: 8,
    marginTop: 14,
    marginLeft: 10,
  },
  classifyView: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    padding: 5,
  },
  fixed: {
    backgroundColor: '#e0e0e0',
  },
  fixedText: {
    color: '#cccccc',
  },

  addView: {
    position: 'absolute',
    top: -5,
    right: -5,
    height: 16,
    width: 16,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f86442',
    borderRadius: 8,
  },
  addText: {
    // marginLeft: 1,
    lineHeight: 15,
    color: '#fff',
  },
  deleteView: {
    position: 'absolute',
    top: -5,
    right: -5,
    height: 16,
    width: 16,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#cccccc',
    borderRadius: 8,
  },

  deleteText: {
    // marginLeft: 1,
    lineHeight: 15,
    color: '#fff',
  },
  myTypeText: {
    marginBottom: 8,
    marginTop: 14,
    fontSize: 18,
  },
  tips: {
    color: '#999999',
    fontSize: 16,
    paddingLeft: 12,
  },
});

export default connect(mapStateToProps)(Category);
