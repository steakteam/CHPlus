# CHPlus

[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) 버전만 맞으면 1.8 이상 모든버전 호환 가능

## 함수

| 함수명                    | 반환 타입 | 인자                                  | 설명                                                        |
| ---                     | ---       | ---                              | ---                                                         |
| set_tab_msg             | void      | [player], header, footer         | 해당 플레이어의 탭창에 헤더와 푸터를 설정합니다.            |
| send_action_msg         | void      | [player], text                   | 해당 플레이어에게 액션 메세지를 보냅니다.                         |
| send_json_msg           | void      | [player], json                   | 해당 플레이어에게 json 메세지(tellraw) 를 보냅니다.                 |
| launch_instant_firework | void      | [locationArray](http://wiki.sk89q.com/wiki/CommandHelper/Array_Formatting#Location_array), [[fireworkArray]](http://wiki.sk89q.com/wiki/CommandHelper/Staged/API/launch_firework#Description) | 해당 위치에 즉시 터지는 폭죽을 발사합니다.
| user_input              | void      | [player], closure, [itemArray]   | 모루 GUI 로 플레이어의 입력값을 closure 로 받습니다. |
| pping                   | int       | [player]                         |  해당 플레이어의 핑(지연시간) 을 불러옵니다.                                       |
| chp_respawn             | void      | [player]                         | 해당 플레이어를 리스폰시킵니다.                                                      |
| inv_title               | string    | [player]                         | 해당 플레이어에 열려있는 탑 인벤토리의 타이틀을 불러옵니다.                          |

## 함수 예제

### set_tab_msg
```coffeescript
set_tab_msg('EntryPoint', 'Header', 'Footer')
```

### send_action_msg
```coffeescript
send_action_msg('EntryPoint', 'This is action message')
```

### send_json_msg
```coffeescript

```

### launch_instant_firework
```coffeescript
launch_instant_firework(array('x': 1, 'y': 2, 'z': 3, 'world': 'world'))
```

### user_input
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

### pping
```coffeescript
@ping = pping('EntryPoint')
if (@ping > 1000) {
  tmsg('EntryPoint', '핑이 너무 높습니다.')
}
```

### chp_respawn
```coffeescript
chp_respawn('EntryPoint')
```

## 이벤트

### server_ping_protocol
일반적인 `server_ping` 이벤트와 동일하지만 이벤트 데이터가 `players`(현재 온라인 플레이어의 수) 밖에 없으며, 이 필드는 수정 가능합니다.(원래 플레이어 수 이상도 가능)