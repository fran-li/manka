package pe.edu.utec.manka.dto;

import jakarta.validation.constraints.NotEmpty;
import pe.edu.utec.manka.entity.TipoRol;

import java.util.Set;

public class UserRolesUpdateRequestDto {

    @NotEmpty
    private Set<TipoRol> roles;

    public Set<TipoRol> getRoles() {
        return roles;
    }

    public void setRoles(Set<TipoRol> roles) {
        this.roles = roles;
    }
}
