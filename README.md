# CHPlus

[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) 버전만 맞으면 1.8 이상 모든버전 호환 가능

## 함수

### set_tab_msg(player, header, footer)

반환값: void

설명: 해당 플레이어의 탭창의 헤더와 푸터를 설정합니다.

예제: set_tab_msg('EntryPoint', 'Header', 'Footer')

### send_action_msg(player, text)

반환값: void

설명: 해당 플레이어에게 액션 메세지를 보냅니다.

예제: send_action_msg('EntryPoint', 'This is action message')

### send_title_msg(player, title, subtitle, fadein, stay, fadeout)

반환값: void

설명: 해당 플레이어에게 타이틀 메세지를 보냅니다.

예제: send_title_msg('EntryPoint', 'Title', 'Subtitle', 20, 20, 20)

### send_json_msg(player, json)

반환값: void

설명: 해당 플레이어에게 json 메세지(tellraw) 를 보냅니다.

### launch_instant_firework([locationArray](http://wiki.sk89q.com/wiki/CommandHelper/Array_Formatting#Location_array), [[fireworkArray]](http://wiki.sk89q.com/wiki/CommandHelper/Staged/API/launch_firework#Description))

반환값: void

설명: 해당 위치에 즉시 터지는 폭죽을 발사합니다.

예제: launch_instant_firework(array('x': 1, 'y': 2, 'z': 3, 'world': 'world'))


