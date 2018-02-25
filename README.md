# CHPlus

[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) 버전만 맞으면 1.8 이상 모든버전 호환 가능

## 함수

### set_tab_msg(player, header, footer)

반환값: void

설명: 해당 플레이어의 탭창의 헤더와 푸터를 설정합니다.

예제: 
```coffeescript
set_tab_msg('EntryPoint', 'Header', 'Footer')
```

### send_action_msg(player, text)

반환값: void

설명: 해당 플레이어에게 액션 메세지를 보냅니다.

예제: send_action_msg('EntryPoint', 'This is action message')

### send_title_msg(player, title, subtitle, fadein, stay, fadeout)

반환값: void

설명: 해당 플레이어에게 타이틀 메세지를 보냅니다.

예제: 
```coffeescript
send_title_msg('EntryPoint', 'Title', 'Subtitle', 20, 20, 20)
````

### send_json_msg(player, json)

반환값: void

설명: 해당 플레이어에게 json 메세지(tellraw) 를 보냅니다.

### launch_instant_firework([locationArray](http://wiki.sk89q.com/wiki/CommandHelper/Array_Formatting#Location_array), [[fireworkArray]](http://wiki.sk89q.com/wiki/CommandHelper/Staged/API/launch_firework#Description))

반환값: void

설명: 해당 위치에 즉시 터지는 폭죽을 발사합니다.

예제: 
```coffeescript
launch_instant_firework(array('x': 1, 'y': 2, 'z': 3, 'world': 'world'))
```

### user_input(player, closure, [itemArray])

반환값: void

설명: 모루 GUI 로 플레이어의 입력값을 closure 로 받습니다.

예제: 
```coffeescript
@callback = closure(@msg) {
  'EntryPoint 님이' @msg '를 입력했습니다.'
}
@item = array(
  id: 421, data: 0, qty: 1,
  meta: array(
    'display': '이곳에 입력하세요.'
  )
)
user_input('EntryPoint', @callback, @item);
```

### pping(player)

반환값: int

설명: 해당 플레이어의 핑 값을 불러옵니다.

예제:
```coffeescript
@ping = pping('EntryPoint')
if (@ping > 1000) {
  tmsg('EntryPoint', '핑이 너무 높습니다.')
}
```

### chp_respawn(player])

반환값: void

설명: 해당 플레이어를 리스폰시킵니다.

예제:
```coffeescript
chp_respawn('EntryPoint')
```

## 이벤트

### server_ping_protocol
일반적인 `server_ping` 이벤트와 동일하지만 이벤트 데이터가 `players`(현재 온라인 플레이어의 수) 밖에 없으며, 이 필드는 수정 가능합니다.(원래 플레이어 수 이상도 가능)