/*******************************************************************************
 *     Copyright (C) 2018 wysohn
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package io.github.wysohn.triggerreactor.bukkit.tools.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public class ExitConfirmPrompt implements Prompt {
	Prompt fallback;
	Runnable postWork;
	public ExitConfirmPrompt(Prompt fallback, Runnable postWork) {
		this.fallback = fallback;
		this.postWork = postWork;
	}

	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg1) {
		if(arg1.equalsIgnoreCase("yes")){
			return Prompt.END_OF_CONVERSATION;
		}else{
			return fallback;
		}
	}

	@Override
	public boolean blocksForInput(ConversationContext arg0) {
		return true;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return ChatColor.GOLD + "종료하려는게 확실합니까?" + ChatColor.RED + "작성중인 구문이 저장되지 않았다면 삭제됩니다!"
				+ ChatColor.GREEN + "yes " + ChatColor.GOLD + "를 입력하여 종료하거나,"
				+ ChatColor.RED + "아무거나 " + ChatColor.BLUE + "입력하여 종료를 취소합니다.";
	}

}
