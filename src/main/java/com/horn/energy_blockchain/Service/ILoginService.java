package com.horn.energy_blockchain.Service;

import com.horn.energy_blockchain.DTO.Login;
import com.horn.energy_blockchain.common.Result;
import com.horn.energy_blockchain.Entity.User;

public interface ILoginService {
    public Result login(Login loginDTO);

    public Result register(User user);

}
