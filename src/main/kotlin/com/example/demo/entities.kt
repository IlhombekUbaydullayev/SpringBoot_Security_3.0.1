package com.example.demo

import jakarta.persistence.*
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.sql.Timestamp
import java.util.*

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
abstract class AbsLongEntity : AbsMainEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var deleted: Boolean? = false
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbsMainEntity {

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private val createdAt: Timestamp? = null

    @UpdateTimestamp
    private val updatedAt: Timestamp? = null

//    @JoinColumn(updatable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private val createdBy: User? = null
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private val updatedBy: User? = null
}

@Entity(name = "users")
@EqualsAndHashCode(callSuper = true)
class User(
    @field:Column(unique = true, nullable = false)
    var email: String,
    var passwords: String = "",
    @Enumerated(EnumType.STRING)
    var systemRoleName: CompanyRoleName? = CompanyRoleName.ROLE_USER,
    var enabled: Boolean = true,
    private val accountNonExpired: Boolean = true,
    private val accountNonLocked: Boolean = true,
    private val credentialsNonExpired: Boolean = true
) :
    AbsLongEntity(), UserDetails {
    private constructor() : this("","",CompanyRoleName.ROLE_USER,true,true,true,true)

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        val simpleGrantedAuthority = SimpleGrantedAuthority(systemRoleName?.name)
        return Collections.singletonList(simpleGrantedAuthority)
    }

    override fun getPassword(): String {
        return this.passwords
    }

    override fun getUsername(): String {
        return this.email
    }

    override fun isAccountNonExpired(): Boolean {
        return this.accountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return this.accountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return this.credentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }
}