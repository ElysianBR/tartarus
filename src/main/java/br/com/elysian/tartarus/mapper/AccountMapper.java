package br.com.elysian.tartarus.mapper;

import br.com.elysian.tartarus.dto.v1.post.RegisterDTO;
import br.com.elysian.tartarus.dto.v1.put.AccountDTO;
import br.com.elysian.tartarus.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

    @Mapper(componentModel = "spring")
    public interface AccountMapper {
        //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void fromAccountDTO(AccountDTO dto, @MappingTarget Account model);
        void fromRegisterDTO(RegisterDTO dto, @MappingTarget Account model);
    }
    