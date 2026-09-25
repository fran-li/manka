package pe.edu.utec.manka.dto;

public class UserRegisterResponseDto {
    private Long id;

    public UserRegisterResponseDto() {}
    public UserRegisterResponseDto(Long id) { this.id = id; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
