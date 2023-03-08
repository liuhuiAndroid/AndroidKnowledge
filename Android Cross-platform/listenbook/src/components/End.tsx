import React from 'react';
import {View, Text, StyleSheet} from 'react-native';

class End extends React.PureComponent {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>我是有底线的！</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    margin: 10,
    alignItems: 'center',
  },
  text: {
    color: '#eee',
  },
});

export default End;
