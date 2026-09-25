package pe.edu.utec.manka.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.utec.manka.dto.UserResponseDto;
import pe.edu.utec.manka.entity.Usuario;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(Usuario.class, UserResponseDto.class)
                .addMappings(mapper -> mapper.skip(UserResponseDto::setRoles));

        return modelMapper;
    }
}
