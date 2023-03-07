package com.study.funny.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.study.funny.R
import com.study.funny.ui.theme.WeComposeTheme

@Composable
fun WeBottomBar(selected: Int, onSelectedChanged: (Int) -> Unit) {
    Row(Modifier.background(color = WeComposeTheme.colors.bottomBar)) {
        TabItem(
            iconId = if (selected == 0) R.drawable.ic_chat_filled else R.drawable.ic_chat_outlined,
            title = "微信",
            tint = if (selected == 0) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged.invoke(0)
                }
        )
        TabItem(
            iconId = if (selected == 1) R.drawable.ic_contacts_filled else R.drawable.ic_contacts_outlined,
            title = "通讯录",
            tint = if (selected == 1) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged.invoke(1)
                }
        )
        TabItem(
            iconId = if (selected == 2) R.drawable.ic_discovery_filled else R.drawable.ic_discovery_outlined,
            title = "发现",
            tint = if (selected == 2) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged.invoke(2)
                }
        )
        TabItem(
            iconId = if (selected == 3) R.drawable.ic_me_filled else R.drawable.ic_me_outlined,
            title = "我",
            tint = if (selected == 3) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged.invoke(3)
                }
        )
    }
}

@Composable
fun TabItem(@DrawableRes iconId: Int, title: String, tint: Color, modifier: Modifier) {
    Column(
        modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = title,
            Modifier.size(24.dp),
            tint = tint
        )
        Text(text = title, fontSize = 11.sp, color = tint)
    }
}

@Preview(showBackground = true)
@Composable
fun WeBottomBarPreviewLight() {
    WeComposeTheme(WeComposeTheme.Theme.Light) {
        var selectedTab by remember { mutableStateOf(0) }
        WeBottomBar(selectedTab) { selectedTab = it}
    }
}

@Preview(showBackground = true)
@Composable
fun WeBottomBarPreviewDark() {
    WeComposeTheme(WeComposeTheme.Theme.Dark) {
        var selectedTab by remember { mutableStateOf(0) }
        WeBottomBar(selectedTab) { selectedTab = it}
    }
}

// 快捷键：prev
@Preview(showBackground = true)
@Composable
fun TabItemPreview() {
    TabItem(
        iconId = R.drawable.ic_chat_filled,
        title = "微信",
        tint = WeComposeTheme.colors.icon,
        modifier = Modifier
    )
}