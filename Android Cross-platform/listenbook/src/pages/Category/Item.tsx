import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {ICategory} from '@/models/category';
import {viewportWidth} from '@/utils/index';

export const parentWidth = viewportWidth - 10;
export const itemWidth = parentWidth / 4;
export const itemHeight = 48;
export const marginTop = 5;

interface IProps {
  fixedItems: number[];
  index: number;
  isEdit: boolean;
  data: ICategory;
  selected: boolean;
  disabled: boolean;
}

class Item extends React.Component<IProps> {
  render() {
    const {data, disabled, isEdit, selected} = this.props;

    return (
      <View key={data.id} style={styles.itemWrapper}>
        <View style={[styles.item, isEdit && disabled && styles.disabled]}>
          <Text>{data.name}</Text>
          {isEdit && !disabled && (
            <View style={styles.icon}>
              <Text style={styles.iconText}>{selected ? '-' : '+'}</Text>
            </View>
          )}
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  itemWrapper: {
    width: itemWidth,
    height: itemHeight,
  },
  item: {
    flex: 1,
    // width: '100%',
    margin: marginTop,
    backgroundColor: '#fff',
    borderRadius: 3,
    justifyContent: 'center',
    alignItems: 'center',
  },
  icon: {
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
  iconText: {
    lineHeight: 15,
    color: '#fff',
  },
  disabled: {
    backgroundColor: '#e2e2e2',
  },
});

export default Item;
