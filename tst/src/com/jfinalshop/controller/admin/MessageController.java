package com.jfinalshop.controller.admin;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Message;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.MessageService;

/**
 * Controller - 消息
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/message")
public class MessageController extends BaseController {

	private MessageService messageService = enhance(MessageService.class);
	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 检查用户名是否合法
	 */
	public void checkUsername() {
		String username = getPara("username");
		if (StringUtils.isEmpty(username)) {
			renderJson(false);
			return;
		}
		renderJson(memberService.usernameExists(username));
	}

	/**
	 * 发送
	 */
	public void send() {
		Long draftMessageId = getParaToLong("draftMessageId");
		Message draftMessage = messageService.find(draftMessageId);
		if (draftMessage != null && draftMessage.getIsDraft() && draftMessage.getSender() == null) {
			setAttr("draftMessage", draftMessage);
		}
		render("/admin/message/send.ftl");
	}

	/**
	 * 发送
	 */
	public void sendSubmit() {
		Long draftMessageId = getParaToLong("draftMessageId");
		String username = getPara("username");
		String title = getPara("title");
		String content = getPara("content");
		Boolean isDraft = getParaToBoolean("isDraft", false);
		Message draftMessage = messageService.find(draftMessageId);
		if (draftMessage != null && draftMessage.getIsDraft() && draftMessage.getSender() == null) {
			messageService.delete(draftMessage);
		}
		Member receiver = null;
		if (StringUtils.isNotEmpty(username)) {
			receiver = memberService.findByUsername(username);
			if (receiver == null) {
				redirect(ERROR_VIEW);
				return;
			}
		}
		
		System.out.println("-->"+receiver.getUsername());
		
		Message message = new Message();
		message.setTitle(title);
		message.setContent(content);
		message.setIp(getRequest().getRemoteAddr());
		message.setIsDraft(isDraft);
		message.setSenderRead(true);
		message.setReceiverRead(false);
		message.setSenderDelete(false);
		message.setReceiverDelete(false);
		message.setSender(null);
		message.setReceiver(receiver);
		message.setReceiverId(receiver.getId());//接受人
		message.setForMessage(null);
		message.setReplyMessages(null);
		messageService.save(message);
		if (isDraft) {
			addFlashMessage(com.jfinalshop.Message.success("admin.message.saveDraftSuccess"));
			redirect("draft.jhtml");
		} else {
			addFlashMessage(com.jfinalshop.Message.success("admin.message.sendSuccess"));
			redirect("list.jhtml");
		}
	}

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		Message message = messageService.find(id);
		if (message == null || message.getIsDraft() || message.getForMessage() != null) {
			redirect(ERROR_VIEW);
			return;
		}
		if ((message.getSenderId() != null && message.getReceiverId() != null) || (message.getReceiverId() == null && message.getReceiverDelete()) || (message.getSenderId() == null && message.getSenderDelete())) {
			redirect(ERROR_VIEW);
			return;
		}
		if (message.getReceiverId() == null) {
			message.setReceiverRead(true);
		} else {
			message.setSenderRead(true);
		}
		messageService.update(message);
		setAttr("adminMessage", message);
		render("/admin/message/view.ftl");
	}

	/**
	 * 回复
	 */
	public void reply() {
		Long id = getParaToLong("adminMessage.id");
		String content = getPara("content");
		
		Message forMessage = messageService.find(id);
		if (forMessage == null || forMessage.getIsDraft() || forMessage.getForMessage() != null) {
			redirect(ERROR_VIEW);
			return;
		}
		if ((forMessage.getSenderId() != null && forMessage.getReceiverId() != null) || (forMessage.getReceiver() == null && forMessage.getReceiverDelete()) || (forMessage.getSender() == null && forMessage.getSenderDelete())) {
			redirect(ERROR_VIEW);
			return;
		}
		Message message = new Message();
		message.setTitle("reply: " + forMessage.getTitle());
		message.setContent(content);
		message.setIp(getRequest().getRemoteAddr());
		message.setIsDraft(false);
		message.setSenderRead(true);
		message.setReceiverRead(false);
		message.setSenderDelete(false);
		message.setReceiverDelete(false);
		message.setSenderId(null);
		message.setReceiverId(forMessage.getReceiverId() == null ? forMessage.getSenderId() : forMessage.getReceiverId());
		if ((forMessage.getReceiverId() == null && !forMessage.getSenderDelete()) || (forMessage.getSender() == null && !forMessage.getReceiverDelete())) {
			message.setForMessage(forMessage);
		}
		message.setReplyMessages(null);
		messageService.save(message);

		if (forMessage.getSenderId() == null) {
			forMessage.setSenderRead(true);
			forMessage.setReceiverRead(false);
		} else {
			forMessage.setSenderRead(false);
			forMessage.setReceiverRead(true);
		}
		messageService.update(forMessage);

		if ((forMessage.getReceiverId() == null && !forMessage.getSenderDelete()) || (forMessage.getSenderId() == null && !forMessage.getReceiverDelete())) {
			addFlashMessage(SUCCESS_MESSAGE);
			redirect("view.jhtml?id=" + forMessage.getId());
		} else {
			addFlashMessage(com.jfinalshop.Message.success("admin.message.replySuccess"));
			redirect("list.jhtml");
		}
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", messageService.findPage(null, pageable, null));
		render("/admin/message/list.ftl");
	}

	/**
	 * 草稿箱
	 */
	public void draft() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", messageService.findDraftPage(null, pageable));
		render("/admin/message/draft.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		if (ids != null) {
			for (Long id : ids) {
				messageService.delete(id, null);
			}
		}
		renderJson(SUCCESS_MESSAGE);
	}

}