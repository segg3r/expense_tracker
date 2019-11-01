package com.segg3r.expensetracker.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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
@EqualsAndHashCode(of = "id")
public class User {
	@Id
	private String id;
	private String name;
	@JsonIgnore
	private String password;
	private List<String> authorities;

	public List<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
		return this.authorities != null
				? this.authorities.stream().map(SimpleGrantedAuthority::new).collect(toList())
				: emptyList();
	}
}
