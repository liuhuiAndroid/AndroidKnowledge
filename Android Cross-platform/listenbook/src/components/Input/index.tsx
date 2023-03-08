import React from 'react';
import {View, Text, StyleSheet, TextInput} from 'react-native';
import {ErrorMessage} from 'formik';

class Input extends React.PureComponent {
  render() {
    const {form, field, ...rest} = this.props;
    return (
      <View style={styles.container}>
        <TextInput
          style={styles.input}
          onChangeText={form.handleChange(field.name)}
          onBlur={form.handleBlur(field.name)}
          {...field}
          {...rest}
        />
        <View>
          <ErrorMessage
            name={field.name}
            component={Text}
            style={styles.error}
          />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 10,
  },
  input: {
    height: 40,
    paddingHorizontal: 10,
    borderColor: '#ccc',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  error: {
    position: 'absolute',
    color: 'red',
    marginTop: 5,
    marginLeft: 10,
    fontSize: 12,
  },
});

export default Input;
