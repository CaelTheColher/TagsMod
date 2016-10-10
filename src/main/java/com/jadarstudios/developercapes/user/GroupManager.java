/**
 * DeveloperCapes by Jadar
 * License: MIT License
 * (https://raw.github.com/jadar/DeveloperCapes/master/LICENSE)
 * version 4.0.0.x
 */
package com.jadarstudios.developercapes.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jadarstudios.developercapes.DevCapes;
import com.jadarstudios.developercapes.cape.CapeManager;

/**
 * All groups have to be managed
 *
 * @author jadar
 */
public class GroupManager {

	protected static GroupManager instance;

	private HashMap<String, Group> groups;

	public GroupManager () {
		this.groups = new HashMap<String, Group>();
	}

	public static GroupManager getInstance () {
		if (instance == null) instance = new GroupManager();
		return instance;
	}

	public void addGroup (Group group) {
		groups.put(group.name, group);

		try {
			UserManager.getInstance().addUsers(group.users.values());
			CapeManager.getInstance().addCape(group.cape);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addGroups (Collection<Group> groups) {
		for (Group g : groups)
			GroupManager.getInstance().addGroup(g);
	}

	public Group getGroup (String capeName) {
		return groups.get(capeName);
	}

	public Group newGroup (String name) {
		if (getGroup(name) != null) return getGroup(name);
		Group group = new Group(name);
		return group;
	}

	public Group parse (String name, Map<String, Object> data) {
		Group group = new Group(name);

		Object usersObj = data.get("users");
		Object capeUrlObj = data.get("capeUrl");

		if (!(usersObj instanceof ArrayList) || !(capeUrlObj instanceof String)) {
			return null;
		}

		ArrayList users = (ArrayList) usersObj;
		String capeUrl = (String) capeUrlObj;

		group.cape = CapeManager.getInstance().parse(name, capeUrl);

		for (Object obj : users) {
			User user = UserManager.getInstance().parse((String) obj, group.cape);
			if (user != null) group.addUser(user);
		}
		return group;
	}
}