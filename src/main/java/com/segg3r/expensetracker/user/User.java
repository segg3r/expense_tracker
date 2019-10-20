package com.segg3r.expensetracker.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class User {
	@Id
	private ObjectId id;
	private String name;
	private String password;
	private List<String> authorities;

	public List<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
		return this.authorities != null
				? this.authorities.stream().map(SimpleGrantedAuthority::new).collect(toList())
				: emptyList();
	}
}
