package org.exampl;


import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "app_users")
public class AppUser {

    public AppUser() {
    }
    public AppUser(String username, String passwordHash, boolean enabled, Set<Role> roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.roles = roles;
    }

    public AppUser(Long id, String username, String passwordHash,
                   boolean enabled, Set<Role> roles ,Long employeeId) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.roles = roles;
        this.employeeId = employeeId;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id", unique = true)
//    private Employee employee;
//@OneToOne(fetch = FetchType.EAGER)
//@JoinColumn(name = "employee_id", unique = true)
//private Employee employee;

    private Long employeeId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }
//    public Long getEmployeeId() {
//        return employee != null ? employee.getId() : null;
//    }


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}


