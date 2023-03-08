/* tslint:disable */
/* eslint-disable */

import React, { FunctionComponent } from 'react';
import { ViewProps } from 'react-native';
import { GProps } from 'react-native-svg';
import IconLoading from './IconLoading';
import IconVolumeUp from './IconVolumeUp';
import IconFavoritesFill from './IconFavoritesFill';
import IconVolumeOff from './IconVolumeOff';
import IconPause from './IconPause';
import IconPlayArrow from './IconPlayArrow';
import IconFullscreen from './IconFullscreen';
import IconLijitingke from './IconLijitingke';
import IconBofang3 from './IconBofang3';
import IconPaste from './IconPaste';
import IconShangyishou from './IconShangyishou';
import IconXiayishou from './IconXiayishou';
import IconDown from './IconDown';
import IconBack from './IconBack';
import IconHuanyipi from './IconHuanyipi';
import IconXihuan from './IconXihuan';
import IconMore from './IconMore';
import IconShengyin from './IconShengyin';
import IconV from './IconV';
import IconUser from './IconUser';
import IconBofang from './IconBofang';
import IconPlay2 from './IconPlay2';
import IconTime from './IconTime';
import IconMessage from './IconMessage';
import IconBofang1 from './IconBofang1';
import IconBofang2 from './IconBofang2';
import IconFaxian from './IconFaxian';
import IconShijian from './IconShijian';
import IconShouye from './IconShouye';
import IconShoucang from './IconShoucang';

export type IconNames = 'icon-loading' | 'icon-volume-up' | 'icon-favorites-fill' | 'icon-volume-off' | 'icon-pause' | 'icon-play-arrow' | 'icon-fullscreen' | 'icon-lijitingke' | 'icon-bofang3' | 'icon-paste' | 'icon-shangyishou' | 'icon-xiayishou' | 'icon-down' | 'icon-back' | 'icon-huanyipi' | 'icon-xihuan' | 'icon-more' | 'icon-shengyin' | 'icon-V' | 'icon-user' | 'icon-bofang' | 'icon-play2' | 'icon-time' | 'icon-message' | 'icon-bofang1' | 'icon-bofang2' | 'icon-faxian' | 'icon-shijian' | 'icon-shouye' | 'icon-shoucang';

interface Props extends GProps, ViewProps {
  name: IconNames;
  size?: number;
  color?: string | string[];
}

const IconFont: FunctionComponent<Props> = ({ name, ...rest }) => {
  switch (name) {
    case 'icon-loading':
      return <IconLoading {...rest} />;
    case 'icon-volume-up':
      return <IconVolumeUp {...rest} />;
    case 'icon-favorites-fill':
      return <IconFavoritesFill {...rest} />;
    case 'icon-volume-off':
      return <IconVolumeOff {...rest} />;
    case 'icon-pause':
      return <IconPause {...rest} />;
    case 'icon-play-arrow':
      return <IconPlayArrow {...rest} />;
    case 'icon-fullscreen':
      return <IconFullscreen {...rest} />;
    case 'icon-lijitingke':
      return <IconLijitingke {...rest} />;
    case 'icon-bofang3':
      return <IconBofang3 {...rest} />;
    case 'icon-paste':
      return <IconPaste {...rest} />;
    case 'icon-shangyishou':
      return <IconShangyishou {...rest} />;
    case 'icon-xiayishou':
      return <IconXiayishou {...rest} />;
    case 'icon-down':
      return <IconDown {...rest} />;
    case 'icon-back':
      return <IconBack {...rest} />;
    case 'icon-huanyipi':
      return <IconHuanyipi {...rest} />;
    case 'icon-xihuan':
      return <IconXihuan {...rest} />;
    case 'icon-more':
      return <IconMore {...rest} />;
    case 'icon-shengyin':
      return <IconShengyin {...rest} />;
    case 'icon-V':
      return <IconV {...rest} />;
    case 'icon-user':
      return <IconUser {...rest} />;
    case 'icon-bofang':
      return <IconBofang {...rest} />;
    case 'icon-play2':
      return <IconPlay2 {...rest} />;
    case 'icon-time':
      return <IconTime {...rest} />;
    case 'icon-message':
      return <IconMessage {...rest} />;
    case 'icon-bofang1':
      return <IconBofang1 {...rest} />;
    case 'icon-bofang2':
      return <IconBofang2 {...rest} />;
    case 'icon-faxian':
      return <IconFaxian {...rest} />;
    case 'icon-shijian':
      return <IconShijian {...rest} />;
    case 'icon-shouye':
      return <IconShouye {...rest} />;
    case 'icon-shoucang':
      return <IconShoucang {...rest} />;
  }

  return null;
};

export default IconFont;
