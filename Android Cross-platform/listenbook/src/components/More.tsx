import React from 'react';
import {View, Text, StyleSheet} from 'react-native';

class More extends React.PureComponent {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>正在加载中。。。</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    margin: 10,
    marginVertical: 30,
    alignItems: 'center',
  },
  text: {
    color: '#333',
  },
});

export default More;
