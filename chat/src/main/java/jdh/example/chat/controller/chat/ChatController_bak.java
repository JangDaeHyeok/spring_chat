package jdh.example.chat.controller.chat;

/**
 * @author 장대혁
 * @date 2022-01-11
 * @description 채팅방 생성 및 조회 RestAPI 구현
 */
// @RequiredArgsConstructor
// @RestController
// @RequestMapping("/chat")
//public class ChatController_bak {
//	@Autowired ChatService chatService;
//	
//	// 채팅방 생성
//	@PostMapping
//	public ChatRoomDTO_bak createRoom(@RequestBody Map<String, Object> input) {
//		String name = "test";
//		if(ValidateUtil.checkNotEmpty(input.get("name"))) name = input.get("name").toString();
//		
//		return chatService.createRoom(name);
//	}
//	
//	// 채팅방 조회
//	@GetMapping
//	public List<ChatRoomDTO_bak> findAllRoom() {
//		return chatService.findAllRoom();
//	}
//}
