package com.study.funny.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.study.funny.MainViewModel
import com.study.funny.data.Chat
import com.study.funny.ui.theme.WeComposeTheme

@Composable
fun ChatList(chats: List<Chat>) {
    Column(
        modifier = Modifier
            .background(WeComposeTheme.colors.background)
            .fillMaxSize()
    ) {
        WeTopBar(title = "微信")
        // LazyColumn -> RecyclerView
        LazyColumn(Modifier.background(WeComposeTheme.colors.listItem)) {
            itemsIndexed(chats) { index, chat ->
                ChatListItem(chat)
                if (index < chats.lastIndex) {
                    Divider(
                        startIndent = 68.dp,
                        color = WeComposeTheme.colors.chatListDivider,
                        thickness = 0.8f.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatListItem(chat: Chat) {
    val viewModel: MainViewModel = viewModel()
    Row(
        Modifier
            .clickable {
                viewModel.startChat(chat)
            }
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = chat.friend.avatar),
            contentDescription = chat.friend.name,
            Modifier
                .padding(8.dp)
                .size(48.dp)
                .unRead(!chat.msgs.last().read, WeComposeTheme.colors.badge)
                .clip(RoundedCornerShape(4.dp)) // shape 还可以切背景
        )
        Column(
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                chat.friend.name,
                fontSize = 17.sp,
                color = WeComposeTheme.colors.textPrimary
            )
            Text(
                chat.msgs.last().text,
                fontSize = 14.sp,
                color = WeComposeTheme.colors.textSecondary
            )
        }
        Text(
            chat.msgs.last().time,
            Modifier.padding(8.dp, 8.dp, 12.dp, 8.dp),
            fontSize = 11.sp, color = WeComposeTheme.colors.textSecondary
        )
    }
}

fun Modifier.unRead(show: Boolean, color: Color): Modifier = this.drawWithContent {
    drawContent()
    if (show) {
        drawCircle(
            color, 5.dp.toPx(),
            Offset(size.width - 1.dp.toPx(), 1.dp.toPx())
        )
    }
}