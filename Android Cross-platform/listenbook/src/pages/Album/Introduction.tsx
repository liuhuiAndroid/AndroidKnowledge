import React from 'react';
import {View, Text, StyleSheet, ScrollView} from 'react-native';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';

const mapStateToProps = ({album}: RootState) => ({
  data: album,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

class Introduction extends React.Component<ModelState> {
  render() {
    const {data} = this.props;
    return (
      <View style={styles.container}>
        <Text style={styles.title}>简介</Text>
        <Text>{data.introduction}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
  title: {
    fontSize: 18,
  },
});

export default connector(Introduction);
